package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpStatus;

public enum AuthExceptionType implements ExceptionType {
    INCORRECT_AUTHORIZATION_CODE("AUTH001", "유효하지 않은 Authorization code입니다.", BAD_REQUEST),
    EMPTY_TOKEN("AUTH002", "올바르지 않은 토큰입니다.", BAD_REQUEST),
    NOT_EQUAL_MEMBER_ID_IN_TOKEN("AUTH003", "올바르지 않은 토큰입니다.", UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("AUTH004", "올바르지 않은 토큰입니다.", UNAUTHORIZED),
    TAMPERING_TOKEN("AUTH005", "올바르지 않은 토큰입니다.", UNAUTHORIZED),
    NOT_EXPIRED_ACCESS_TOKEN("AUTH006", "Access-Token이 만료되지 않았습니다.", UNAUTHORIZED);

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
    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}
