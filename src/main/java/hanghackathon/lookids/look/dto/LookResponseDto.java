package hanghackathon.lookids.look.dto;

import hanghackathon.lookids.global.constant.LookType;

import java.util.List;

public record LookResponseDto(
        LookType lookType,
        List<String> imageUrl,
        String title,
        String content,
        String nickname,
        Integer likesCount
) {
    public static LookResponseDto of(
            LookType lookType,
            List<String> imageUrl,
            String title,
            String content,
            String nickname,
            Integer likesCount
    ) {
        return new LookResponseDto(
                lookType,
                imageUrl,
                title,
                content,
                nickname,
                likesCount
        );
    }
}
