package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.http.HttpStatus;

public enum NotificationExceptionType implements ExceptionType {

    NOTIFICATION_ID_IS_NEGATIVE("NOTIFICATION001", "알림 id는 양수여야 합니다.", BAD_REQUEST),
    NOTIFICATION_NOT_FOUND("NOTIFICATION002", "존재하지 않은 알림입니다.", BAD_REQUEST),
    OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION("NOTIFICATION003", "다른 사용자의 알림을 삭제할 수 없습니다.", FORBIDDEN),
    EMPTY_NOTIFICATION_INFO("NOTIFICATION004", "요청한 알림정보 필드값이 비어있습니다.", BAD_REQUEST),
    PATH_VARIABLE_EMPTY_NOTIFICATION_ID("SHARE012", "PathVariable의 notificationId가 비어있습니다.", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    NotificationExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
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
