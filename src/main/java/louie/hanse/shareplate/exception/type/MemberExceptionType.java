package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public enum MemberExceptionType implements ExceptionType {
    EMPTY_ACCESS_TOKEN("MEMBER001", "올바르지 않은 토큰입니다.", BAD_REQUEST),
    VERIFICATION_ACCESS_TOKEN("MEMBER002", "올바르지 않은 토큰입니다.", UNAUTHORIZED),
    MEMBER_NOT_FOUND("MEMBER003", "올바르지 않은 토큰입니다.", BAD_REQUEST),
    NOT_SUPPORT_IMAGE_TYPE("MEMBER004", "이미지 형식의 파일이 아닙니다.", BAD_REQUEST),
    EMPTY_MEMBER_INFO("MEMBER005", "요청한 필드값이 비어있습니다.", BAD_REQUEST),
    EMPTY_LOCATION("MEMBER006", "요청한 필드값이 비어있습니다.", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    MemberExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
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
