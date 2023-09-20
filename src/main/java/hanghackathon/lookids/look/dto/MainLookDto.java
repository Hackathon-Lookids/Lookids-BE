package hanghackathon.lookids.look.dto;

import org.springframework.data.domain.Slice;

import java.util.List;

public record MainLookDto(
        List<LookResponseDto> mostLikedLooks,
        Slice<LookResponseDto> randomLooks
) {
    public static MainLookDto of(
            List<LookResponseDto> mostLikedLooks,
            Slice<LookResponseDto> randomLooks
    ) {
        return new MainLookDto(
                mostLikedLooks,
                randomLooks
        );
    }
}
