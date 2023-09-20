package hanghackathon.lookids.global.exception;

import hanghackathon.lookids.global.constant.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final String message;
    private final int statusCode;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode exceptionEnum) {
        return ResponseEntity.status(exceptionEnum.getHttpStatus()).body(ErrorResponse.builder().message(exceptionEnum.getMessage()).statusCode(exceptionEnum.getHttpStatus().value()).build());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode exceptionEnum, String message) {
        return ResponseEntity.status(exceptionEnum.getHttpStatus()).body(ErrorResponse.builder().message(message).statusCode(exceptionEnum.getHttpStatus().value()).build());
    }
}
