package louie.hanse.shareplate.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@RequiredArgsConstructor
@Component
public class StompErrorHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
        Throwable ex) {
        ExceptionType exceptionType = findExceptionType(ex.getMessage());
        if (exceptionType != null) {
            GlobalExceptionResponse response = new GlobalExceptionResponse(
                new GlobalException(exceptionType));
            byte[] payload;
            try {
                payload = objectMapper.writeValueAsBytes(response);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
            return MessageBuilder.createMessage(payload, accessor.getMessageHeaders());
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private ExceptionType findExceptionType(String message) {
        List<ExceptionType> exceptionTypes = createExceptionTypes();
        for (ExceptionType exceptionType : exceptionTypes) {
            if (message.contains(exceptionType.getMessage())) {
                return exceptionType;
            }
        }
        return null;
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
