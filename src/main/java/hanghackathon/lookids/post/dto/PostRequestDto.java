package hanghackathon.lookids.post.dto;

import hanghackathon.lookids.global.constant.PostType;

public record PostRequestDto(
        PostType postType,
        String title,
        String content,
        String location,
        String price
) {
    public static PostRequestDto of(
            PostType postType,
            String title,
            String content,
            String location,
            String price
    ) {
        return new PostRequestDto(
                postType,
                title,
                content,
                location,
                price
        );
    }
}
