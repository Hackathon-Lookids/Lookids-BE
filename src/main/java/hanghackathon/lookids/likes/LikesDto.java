package hanghackathon.lookids.likes;

public record LikesDto (
        int likeCount,
        boolean likeStatus
) {
    public static LikesDto of(
            int likeCount,
            boolean likeStatus
    ) {
        return new LikesDto(
                likeCount,
                likeStatus
        );
    }
}