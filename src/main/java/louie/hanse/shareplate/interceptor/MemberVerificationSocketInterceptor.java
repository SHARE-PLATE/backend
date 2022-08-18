package louie.hanse.shareplate.interceptor;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@RequiredArgsConstructor
@Slf4j
public class MemberVerificationSocketInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (headerAccessor.getCommand().equals(StompCommand.SEND)) {
            String accessToken = headerAccessor.getNativeHeader(HttpHeaders.AUTHORIZATION).get(0);
            jwtProvider.verifyAccessToken(accessToken);
            Long memberId = jwtProvider.decodeMemberId(accessToken);
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            sessionAttributes.put("memberId", memberId);
        }
        return message;
    }

}
