package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.HttpStatus;

public enum ChatRoomExceptionType implements ExceptionType {
    SHARE_WRITER_CANNOT_LEAVE("CHATROOM001", "쉐어 작성자는 채팅을 나갈 수 없습니다.", BAD_REQUEST),
    INCORRECT_TYPE_VALUE("CHATROOM002", "올바르지 않은 ChatRoom type입니다.", BAD_REQUEST),
    PATH_VARIABLE_EMPTY_CHATROOM_ID("CHATROOM003", "PathVariable의 ChatroomId가 비어있습니다.", BAD_REQUEST),
    CHATROOM_ID_IS_NEGATIVE("CHATROOM004", "채팅방 id는 양수여야 합니다.", BAD_REQUEST),
    CHAT_ROOM_NOT_FOUND("CHATROOM005", "존재하지 않는 채팅방입니다.", BAD_REQUEST),
    CHAT_ROOM_NOT_JOINED("CHATROOM006", "회원이 참여하지 않은 채팅방입니다.", BAD_REQUEST),
    EMPTY_CHATROOM_INFO("CHATROOM007", "요청한 채팅방정보 필드값이 비어있습니다.", BAD_REQUEST),
    WRITER_CAN_NOT_QUESTION_CHAT("CHATROOM008", "내가 등록한 쉐어에는 일대일 채팅을 할 수 없습니다.", BAD_REQUEST),
    CLOSE_TO_THE_CLOSED_DATE_TIME("CHATROOM009", "모집시간 1시간 미만으로 남은 경우, 채팅을 나갈 수 없습니다.", BAD_REQUEST),
    QUESTION_CHAT_ROOM_ALREADY_EXIST("CHATROOM010", "이미 존재하는 문의 채팅방으로 중복참여할 수 없습니다.", BAD_REQUEST);

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
