package hanghackathon.lookids.comment.dto;

import hanghackathon.lookids.comment.Comment;

public record CommentResponseDto(
        String nickname,
        String content
) {
    public static CommentResponseDto of(
            String nickname,
            String content
    ) {
        return new CommentResponseDto(
                nickname,
                content
        );
    }

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.of(
                comment.getUser().getNickname(),
                comment.getContent()
        );
    }
}
