package hanghackathon.lookids.post;

import hanghackathon.lookids.comment.Comment;
import hanghackathon.lookids.comment.CommentRepository;
import hanghackathon.lookids.comment.dto.CommentRequestDto;
import hanghackathon.lookids.comment.dto.CommentResponseDto;
import hanghackathon.lookids.global.S3Util;
import hanghackathon.lookids.global.exception.CustomException;
import hanghackathon.lookids.global.security.UserDetailsImpl;
import hanghackathon.lookids.post.dto.PostRequestDto;
import hanghackathon.lookids.post.dto.PostResponseDto;
import hanghackathon.lookids.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static hanghackathon.lookids.global.constant.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final S3Util s3Util;

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAll().stream()
                .map((post) -> PostResponseDto.of(
                        post,
                        commentRepository.findAllByPostId(post.getId()).stream()
                                .map(CommentResponseDto::from)
                                .toList()
                )).toList();
    }

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, List<MultipartFile> images, UserDetailsImpl userDetails) throws IOException {
        if (userDetails == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        User user = userDetails.getUser();

        List<String> imageUrls = s3Util.uploadFile(images);

        Post post = postRepository.save(
                Post.of(
                        user,
                        postRequestDto.postType(),
                        imageUrls,
                        postRequestDto.title(),
                        postRequestDto.content(),
                        postRequestDto.location(),
                        postRequestDto.price()
                )
        );
        return PostResponseDto.of(post, null);
    }

    public CommentResponseDto commentPost(Long postId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("no such post exists")
        );

        Comment comment = commentRepository.save(
                Comment.of(
                        commentRequestDto.content(),
                        userDetails.getUser(),
                        post
                )
        );
        return CommentResponseDto.from(comment);
    }
}
