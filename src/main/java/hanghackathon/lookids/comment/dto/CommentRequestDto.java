package hanghackathon.lookids.comment.dto;

public record CommentRequestDto(
        String content
) {
    public static CommentRequestDto of(
            String content
    ) {
        return new CommentRequestDto(
                content
        );
    }
}
