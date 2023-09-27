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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static hanghackathon.lookids.global.constant.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LookService {

    private final LookRepository lookRepository;
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
                    return LookResponseDto.of(look, isLiked);
                }).toList();

        return MainLookDto.of(
                mostLikedLooks,
                lookRepository.paginationByRandom(type, lastLookId, pageable, userDetails)
        );
    }

    @Transactional
    public LookResponseDto createLook(LookRequestDto lookRequestDto, List<MultipartFile> images, UserDetailsImpl userDetails) throws IOException {
        if (userDetails == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        User user = userDetails.getUser();

        List<String> imageUrls = s3Util.uploadFile(images);

        Look look = lookRepository.save(
                Look.builder()
                        .user(user)
                        .lookType(lookRequestDto.lookType())
                        .imageUrls(imageUrls)
                        .title(lookRequestDto.title())
                        .content(lookRequestDto.content())
                        .build()
        );

        return LookResponseDto.of(look, false);
    }

    @Transactional
    public LikesDto likeLook(Long lookId, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        User user = userDetails.getUser();

        Look look = lookRepository.findById(lookId)
                .orElseThrow(() -> new IllegalArgumentException("no such look exists"));

        Optional<Likes> optionalLikes = likesRepository.findByUserIdAndLookId(user.getId(), lookId);

        Likes likes;
        if (optionalLikes.isPresent()) {
            likes = optionalLikes.get();
            if (likes.isLikeStatus()) {
                likes.setLikeStatus(false);
                look.setLikeCount(look.getLikeCount() - 1);
            } else {
                likes.setLikeStatus(true);
                look.setLikeCount(look.getLikeCount() + 1);
            }
        } else {
            likes = likesRepository.save(Likes.of(true, user, look));
            look.setLikeCount(look.getLikeCount() + 1);
        }
        return LikesDto.of(look.getLikeCount(), likes.isLikeStatus());
    }
}
