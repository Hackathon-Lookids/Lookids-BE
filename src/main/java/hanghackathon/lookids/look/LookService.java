package hanghackathon.lookids.look;

import hanghackathon.lookids.global.S3Util;
import hanghackathon.lookids.global.constant.LookType;
import hanghackathon.lookids.global.exception.CustomException;
import hanghackathon.lookids.global.security.UserDetailsImpl;
import hanghackathon.lookids.likes.Likes;
import hanghackathon.lookids.likes.LikesDto;
import hanghackathon.lookids.likes.LikesRepository;
import hanghackathon.lookids.look.dto.LookRequestDto;
import hanghackathon.lookids.look.dto.LookResponseDto;
import hanghackathon.lookids.look.dto.MainLookDto;
import hanghackathon.lookids.look.repository.LookRepository;
import hanghackathon.lookids.user.User;
import hanghackathon.lookids.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static hanghackathon.lookids.global.constant.ErrorCode.BAD_REQUEST;
import static hanghackathon.lookids.global.constant.ErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class LookService {

    private final LookRepository lookRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final S3Util s3Util;

    @Transactional(readOnly = true)
    public MainLookDto getLooks(String type, Long lastLookId, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.ofSize(10);

        List<LookResponseDto> mostLikedLooks = lookRepository
                .findTop10ByLookTypeOrderByLikeCountDesc(LookType.valueOf(type.toUpperCase()))
                .stream()
                .map((look) -> {
                    boolean isLiked = false;
                    if (userDetails != null) {
                        Long userId = userDetails.getUser().getId();
                        isLiked = likesRepository.getLikeStatusByUserAndLook(userId, look.getId());
                    }
                    return LookResponseDto.toDto(look, isLiked);
                }).toList();

        return MainLookDto.of(
                mostLikedLooks,
                lookRepository.paginationByRandom(type, lastLookId, pageable, userDetails)
        );
    }

    @Transactional
    public LookResponseDto createLook(LookRequestDto lookRequestDto, List<MultipartFile> images, UserDetailsImpl userDetails) throws IOException {
        User user = userDetails.getUser();
        userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(UNAUTHORIZED)
        );

        List<String> imageUrls = s3Util.uploadFile(images);

        Look look = Look.builder()
                .user(user)
                .lookType(lookRequestDto.lookType())
                .imageUrls(imageUrls)
                .title(lookRequestDto.title())
                .content(lookRequestDto.content())
                .build();

        lookRepository.save(look);

        return LookResponseDto.toDto(look, false);
    }

    @Transactional
    public LikesDto likeLook(Long lookId, UserDetailsImpl userDetails) {
        Look look = lookRepository.findById(lookId)
                .orElseThrow(() -> new IllegalArgumentException("no such look exists"));

        Likes likes = likesRepository.findByUserIdAndLookId(userDetails.getUser().getId(), lookId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));

        likes.setLikeStatus(!likes.isLikeStatus());
        look.setLikeCount(look.getLikeCount() + 1);

        return LikesDto.of(look.getLikeCount(), likes.isLikeStatus());
    }
}
