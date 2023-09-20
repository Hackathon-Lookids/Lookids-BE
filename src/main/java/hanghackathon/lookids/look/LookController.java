package hanghackathon.lookids.look;

import hanghackathon.lookids.global.ResponseDto;
import hanghackathon.lookids.look.dto.LookResponseDto;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/looks")
public class LookController {

    private final LookService lookService;

    public ResponseDto<Slice<LookResponseDto>> getRandomLooks(
            @RequestParam String type,
            @RequestParam @Nullable Long lastLookId) {
        return lookService.getRandomLooks(type, lastLookId);
    }

}
