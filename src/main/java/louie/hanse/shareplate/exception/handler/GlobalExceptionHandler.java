package louie.hanse.shareplate.exception.handler;

import javax.servlet.http.HttpServletResponse;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.web.dto.exception.GlobalExceptionResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public GlobalExceptionResponse globalExceptionResponse(GlobalException globalException, HttpServletResponse response) {
        response.setStatus(globalException.getStatusCode());
        return new GlobalExceptionResponse(globalException);
    }
}
