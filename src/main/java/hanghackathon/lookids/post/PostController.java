package hanghackathon.lookids.post;

import hanghackathon.lookids.comment.dto.CommentRequestDto;
import hanghackathon.lookids.comment.dto.CommentResponseDto;
import hanghackathon.lookids.global.ResponseDto;
import hanghackathon.lookids.global.security.UserDetailsImpl;
import hanghackathon.lookids.post.dto.PostRequestDto;
import hanghackathon.lookids.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseDto<List<PostResponseDto>> getPosts() {
        return ResponseDto.setSuccess(
                "조회 완료", postService.getPosts()
        );
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseDto<PostResponseDto> createPost(
            @RequestPart(value = "post") PostRequestDto postRequestDto,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        return ResponseDto.setSuccess(
                "작성 완료", postService.createPost(postRequestDto, images, userDetails)
        );
    }

    @PostMapping("/{postId}/comments")
    public ResponseDto<CommentResponseDto> commentPost(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        return ResponseDto.setSuccess(
                "작성 완료", postService.commentPost(postId, commentRequestDto, userDetails)
        );
    }
}
