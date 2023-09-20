package hanghackathon.lookids.look.dto;

import hanghackathon.lookids.global.constant.LookType;

public record LookRequestDto(
        LookType lookType,
        String title,
        String content
) {
    public static LookRequestDto of(
            LookType lookType,
            String title,
            String content
    ) {
        return new LookRequestDto(
                lookType,
                title,
                content
        );
    }
}
