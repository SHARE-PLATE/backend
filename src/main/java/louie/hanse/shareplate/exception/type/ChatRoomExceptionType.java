package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.HttpStatus;

public enum ChatRoomExceptionType implements ExceptionType {
    SHARE_WRITER_CANNOT_LEAVE("CHATROOM001", "쉐어 작성자는 채팅을 나갈 수 없습니다.", BAD_REQUEST),
    INCORRECT_TYPE_VALUE("CHATROOM002", "올바르지 않은 ChatRoom type입니다.", BAD_REQUEST),
    CHAT_ROOM_NOT_FOUND("CHATROOM005", "존재하지 않는 채팅방입니다.", BAD_REQUEST),
    EMPTY_CHATROOM_INFO("CHATROOM007", "요청한 채팅방정보 필드값이 비어있습니다.", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    ChatRoomExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
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
