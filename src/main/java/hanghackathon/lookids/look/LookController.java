package hanghackathon.lookids.look;

import hanghackathon.lookids.global.ResponseDto;
import hanghackathon.lookids.global.security.UserDetailsImpl;
import hanghackathon.lookids.look.dto.LookRequestDto;
import hanghackathon.lookids.look.dto.MainLookDto;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/looks")
public class LookController {

    private final LookService lookService;

    @GetMapping
    public ResponseDto<MainLookDto> getLooks(
            @RequestParam String type,
            @RequestParam @Nullable Long lastLookId,
            @AuthenticationPrincipal @Nullable UserDetailsImpl userDetails
    ) {
        return ResponseDto.setSuccess(
                "조회 완료", lookService.getLooks(type, lastLookId, userDetails)
        );
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseDto<?> createLook(
            @RequestPart LookRequestDto lookRequestDto,
            @RequestPart(value = "imageFile", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        return ResponseDto.setSuccess(
                "작성 완료", lookService.createLook(lookRequestDto, images, userDetails)
        );
    }

    @PatchMapping("/{lookId}/likes")
    public ResponseDto<?> likeLook(
            @PathVariable Long lookId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseDto.setSuccess("좋아요 성공", lookService.likeLook(lookId, userDetails));
    }
}
