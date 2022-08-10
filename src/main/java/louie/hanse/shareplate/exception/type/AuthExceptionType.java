package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpStatus;

public enum AuthExceptionType implements ExceptionType {
    INCORRECT_AUTHORIZATION_CODE("AUTH001", "유효하지 않은 Authorization code 입니다.", BAD_REQUEST),
    EMPTY_TOKEN("AUTH002", "비어있는 토큰입니다.", BAD_REQUEST),
    NOT_EQUAL_MEMBER_ID_IN_TOKEN("AUTH003", "올바르지 않은 토큰입니다.", UNAUTHORIZED),
    TAMPERING_TOKEN("AUTH004", "변조된 토큰입니다.", UNAUTHORIZED),
    EMPTY_ACCESS_TOKEN("AUTH005", "비어있는 Access-Token 입니다.", BAD_REQUEST),
    EXPIRED_ACCESS_TOKEN("AUTH006", "만료된 Access-Token 입니다.", UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN("AUTH007", "만료된 Refresh-Token 입니다.", UNAUTHORIZED),
    TAMPERING_ACCESS_TOKEN("AUTH008", "변조된 Access-Token 입니다.", UNAUTHORIZED),
    TAMPERING_REFRESH_TOKEN("AUTH009", "변조된 Refresh-Token 입니다.", UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("AUTH010", "올바르지 않은 Refresh-Token 입니다.", UNAUTHORIZED),
    NOT_EXPIRED_ACCESS_TOKEN("AUTH011", "만료되지 않은 Access-Token 입니다.", UNAUTHORIZED);
;

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    AuthExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatusCode() {
        return httpStatusCode;
    }
}
