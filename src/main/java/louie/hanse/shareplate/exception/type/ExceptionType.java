package louie.hanse.shareplate.exception.type;

import org.springframework.http.HttpStatus;

public interface ExceptionType {
    String getErrorCode();

    String getMessage();

    HttpStatus getHttpStatusCode();
}
