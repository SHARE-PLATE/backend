package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.HttpStatus;

public enum EntryExceptionType implements ExceptionType {
    SHARE_ALREADY_JOINED("ENTRY001", "이미 참가된 쉐어입니다.", BAD_REQUEST),
    SHARE_NOT_JOINED("ENTRY002", "참가되어 있지 않은 쉐어입니다.", BAD_REQUEST),
    SHARE_OVERCAPACITY("ENTRY003", "모집정원이 초과된 쉐어입니다.", BAD_REQUEST),
    CLOSE_TO_THE_CLOSED_DATE_TIME("ENTRY004", "모집시간 1시간 미만으로 남은 경우, 참가 취소를 할 수 없습니다.", BAD_REQUEST),
    CLOSED_DATE_TIME_HAS_PASSED("ENTRY005", "모집이 지난 쉐어의 경우, 참가 취소를 할 수 없습니다.", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    EntryExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
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
