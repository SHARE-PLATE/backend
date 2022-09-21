package louie.hanse.shareplate.exception.type;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.http.HttpStatus;

public enum ShareExceptionType implements ExceptionType {
    EMPTY_SHARE_INFO("SHARE001", "요청한 쉐어정보 값이 비어있습니다.", BAD_REQUEST),
    IMAGE_LIMIT_EXCEEDED("SHARE002", "이미지 5개를 초과하였습니다.", BAD_REQUEST),
    NOT_SUPPORT_IMAGE_TYPE("SHARE003", "쉐어 이미지 형식의 파일이 아닙니다.", BAD_REQUEST),
    SHARE_INFO_IS_NEGATIVE("SHARE004", "요청값은 양수여야 합니다.", BAD_REQUEST),
    PAST_CLOSED_DATE_TIME("SHARE005", "약속 시간은 현재 시간 이후로 설정해야 합니다.", BAD_REQUEST),
    OUT_OF_SCOPE_FOR_KOREA("SHARE006", "해당 위도, 경도는 대한민국의 범위를 벗어났습니다.", BAD_REQUEST),
    INCORRECT_TYPE_VALUE("SHARE008", "올바르지 않은 type입니다.", BAD_REQUEST),
    INCORRECT_MINE_VALUE("SHARE009", "올바르지 않은 mine값입니다.", BAD_REQUEST),
    SHARE_ID_IS_NEGATIVE("SHARE010", "쉐어 id는 양수여야 합니다.", BAD_REQUEST),
    SHARE_NOT_FOUND("SHARE011", "존재하지 않는 쉐어입니다.", BAD_REQUEST),
    IS_NOT_WRITER("SHARE012", "쉐어를 작성한 사람만 쉐어를 편집할 수 있습니다.", FORBIDDEN),
    PATH_VARIABLE_EMPTY_SHARE_ID("SHARE013", "PathVariable의 shareId가 비어있습니다.", BAD_REQUEST),
    SHARE_IS_CLOSED("SHARE014", "마감된 쉐어입니다.", BAD_REQUEST),
    SHARE_IS_CANCELED("SHARE015", "취소된 쉐어입니다.", BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatusCode;

    ShareExceptionType(String errorCode, String message, HttpStatus httpStatusCode) {
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
