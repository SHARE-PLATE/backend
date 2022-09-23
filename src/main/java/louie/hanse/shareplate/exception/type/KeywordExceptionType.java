package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.HttpStatus;

public enum KeywordExceptionType implements ExceptionType {
    EMPTY_KEYWORD_INFO("KEYWORD001", "요청한 키워드정보 필드값이 비어있습니다.", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    KeywordExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
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
