package hanghackathon.lookids.global.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST */
    BAD_REQUEST("400", HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR("400.a", HttpStatus.BAD_REQUEST, "Validation error"),
    FILE_SIZE_OVER("400.b", HttpStatus.BAD_REQUEST, "File capacity must be less than 10MB"),

    /* 401 UNAUTHORIZED */
    UNAUTHORIZED("401", HttpStatus.UNAUTHORIZED, "Unauthorized"),

    /* 403 FORBIDDEN */
    FORBIDDEN("403", HttpStatus.FORBIDDEN, "Forbidden"),
    INVALID_TOKEN("403.a", HttpStatus.FORBIDDEN, "Invalid Token"),

    /* 404 NOT_FOUND */
    NOT_FOUND("404", HttpStatus.NOT_FOUND, "Requested resource is not found"),
    USER_NOT_FOUND("404.a", HttpStatus.NOT_FOUND, "Corresponding user is not found"),

    /* 409 CONFLICT */
    CONFLICT("409", HttpStatus.CONFLICT, "Conflict occurred"),
    DUPLICATE_DATA("409.a", HttpStatus.CONFLICT, "Duplicate data exists")
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
