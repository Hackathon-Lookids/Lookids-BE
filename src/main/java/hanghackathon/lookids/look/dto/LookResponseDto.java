package hanghackathon.lookids.look.dto;

import hanghackathon.lookids.global.constant.LookType;
import hanghackathon.lookids.look.Look;

import java.util.List;

public record LookResponseDto(
        LookType lookType,
        List<String> imageUrls,
        String title,
        String content,
        String nickname,
        Integer likeCount,
        boolean likeStatus
) {
    public static LookResponseDto of(
            LookType lookType,
            List<String> imageUrls,
            String title,
            String content,
            String nickname,
            Integer likeCount,
            boolean likeStatus
    ) {
        return new LookResponseDto(
                lookType,
                imageUrls,
                title,
                content,
                nickname,
                likeCount,
                likeStatus
        );
    }

    public static LookResponseDto toDto(Look look, boolean likeStatus) {
        return LookResponseDto.of(
                look.getLookType(),
                look.getImageUrls(),
                look.getTitle(),
                look.getContent(),
                look.getUser().getNickname(),
                look.getLikeCount(),
                likeStatus
        );
    }
}
