package hanghackathon.lookids.post.dto;

import hanghackathon.lookids.comment.dto.CommentResponseDto;
import hanghackathon.lookids.global.constant.PostType;
import hanghackathon.lookids.post.Post;

import java.util.List;

public record PostResponseDto(
        PostType postType,
        List<String> imageUrls,
        String title,
        String content,
        String nickname,
        String location,
        String price,
        List<CommentResponseDto> comments
) {
    public static PostResponseDto of(
            PostType postType,
            List<String> imageUrls,
            String title,
            String content,
            String nickname,
            String location,
            String price,
            List<CommentResponseDto> comments
    ) {
        return new PostResponseDto(
                postType,
                imageUrls,
                title,
                content,
                nickname,
                location,
                price,
                comments
        );
    }

    public static PostResponseDto of(Post post, List<CommentResponseDto> comments) {
        return PostResponseDto.of(
                post.getPostType(),
                post.getImageUrls(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getLocation(),
                post.getPrice(),
                comments
        );
    }
}
