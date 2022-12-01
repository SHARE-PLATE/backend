package louie.hanse.shareplate.interceptor;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;
import static org.springframework.messaging.simp.stomp.StompCommand.SEND;
import static org.springframework.messaging.simp.stomp.StompCommand.SUBSCRIBE;

import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.AuthExceptionType;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@RequiredArgsConstructor
public class MemberVerificationSocketInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final List<StompCommand> stompCommands = initStompCommands();

    // TODO: 구독하는 채팅방이나 알림이 자신의 것이 아니라면 ERROR 반환

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = headerAccessor.getCommand();
        if (stompCommands.contains(command)) {

            if (!headerAccessor.containsNativeHeader(HttpHeaders.AUTHORIZATION)) {
                throw new GlobalException(AuthExceptionType.EMPTY_ACCESS_TOKEN);
            }
            String accessToken = headerAccessor.getNativeHeader(HttpHeaders.AUTHORIZATION).get(0);
            try {
                jwtProvider.verifyAccessToken(accessToken);
            } catch (JWTVerificationException e) {
                throw new GlobalException(AuthExceptionType.EXPIRED_ACCESS_TOKEN);
            }

            Long memberId = jwtProvider.decodeMemberId(accessToken);
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            sessionAttributes.put("memberId", memberId);
        }
        return message;
    }

    private List<StompCommand> initStompCommands() {
        return List.of(CONNECT, SEND, SUBSCRIBE);
    }
}
