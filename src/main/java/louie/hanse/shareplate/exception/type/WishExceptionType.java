package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.HttpStatus;

public enum WishExceptionType implements ExceptionType {

    SHARE_ALREADY_WISH("WISH001", "이미 찜한 쉐어입니다.", BAD_REQUEST),
    SHARE_NOT_WISH("WISH002", "찜하지 않은 쉐어입니다.", BAD_REQUEST),
    WRITER_CAN_NOT_WISH("WISH003", "내가 등록한 쉐어에는 찜할 수 없습니다.", BAD_REQUEST),
    WRITER_CAN_NOT_WISH_CANCEL("WISH004", "내가 등록한 쉐어에는 찜 취소 할 수 없습니다.", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    WishExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
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
