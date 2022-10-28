package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.http.HttpStatus;

public enum KeywordExceptionType implements ExceptionType {
    EMPTY_KEYWORD_INFO("KEYWORD001", "요청한 키워드정보 필드값이 비어있습니다.", BAD_REQUEST),
    KEYWORD_ID_IS_NEGATIVE("KEYWORD002", "키워드 id는 양수여야 합니다.", BAD_REQUEST),
    KEYWORD_NOT_FOUND("KEYWORD003", "존재하지 않는 키워드입니다.", BAD_REQUEST),
    PATH_VARIABLE_EMPTY_KEYWORD_ID("KEYWORD004", "PathVariable의 keywordId가 비어있습니다.", BAD_REQUEST),
    OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD("KEYWORD005", "다른 사용자의 키워드를 삭제할 수 없습니다.", FORBIDDEN),
    DUPLICATE_KEYWORD("KEYWORD006", "이미 등록된 키워드입니다.", BAD_REQUEST);

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
