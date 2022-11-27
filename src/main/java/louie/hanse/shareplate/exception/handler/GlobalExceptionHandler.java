package louie.hanse.shareplate.exception.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.AuthExceptionType;
import louie.hanse.shareplate.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.exception.type.EntryExceptionType;
import louie.hanse.shareplate.exception.type.ExceptionType;
import louie.hanse.shareplate.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.exception.type.NotificationExceptionType;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.exception.type.WishExceptionType;
import louie.hanse.shareplate.web.dto.exception.GlobalExceptionResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public GlobalExceptionResponse globalExceptionResponse(GlobalException globalException,
        HttpServletResponse response) {
        response.setStatus(globalException.getStatusCode());
        return new GlobalExceptionResponse(globalException);
    }

    @ExceptionHandler({ValidationException.class, BindException.class,
        MethodArgumentNotValidException.class, TypeMismatchException.class})
    public ResponseEntity<GlobalExceptionResponse> exceptionResponse(Exception exception) {
        String message = getMessage(exception);
        ExceptionType exceptionType = findExceptionType(message);
        return ResponseEntity.status(exceptionType.getStatusCode())
            .body(new GlobalExceptionResponse(new GlobalException(exceptionType)));
    }

    private ExceptionType findExceptionType(String message) {
        List<ExceptionType> exceptionTypes = createExceptionTypes();
        for (ExceptionType exceptionType : exceptionTypes) {
            if (message.contains(exceptionType.getMessage())) {
                return exceptionType;
            }
        }
//        TODO : ExceptionType을 찾지 못하는 경우 예외 처리
        return null;
    }

    private String getMessage(Exception ex) {
        return ex.getMessage();
    }

    private List<ExceptionType> createExceptionTypes() {
        List<ExceptionType> exceptionTypes = new ArrayList<>();
        exceptionTypes.addAll(
            Arrays.stream(AuthExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(EntryExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(MemberExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(ShareExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(WishExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(NotificationExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(KeywordExceptionType.values()).collect(Collectors.toList()));
        exceptionTypes.addAll(
            Arrays.stream(ChatRoomExceptionType.values()).collect(Collectors.toList()));
        return exceptionTypes;
    }
}
