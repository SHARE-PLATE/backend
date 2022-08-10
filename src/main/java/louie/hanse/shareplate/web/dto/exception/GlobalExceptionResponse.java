package louie.hanse.shareplate.web.dto.exception;

import lombok.Getter;
import louie.hanse.shareplate.exception.GlobalException;

@Getter
public class GlobalExceptionResponse {
    private String errorCode;
    private String message;

    public GlobalExceptionResponse(GlobalException globalException) {
        this.errorCode = globalException.getErrorCode();
        this.message = globalException.getMessage();
    }
}
