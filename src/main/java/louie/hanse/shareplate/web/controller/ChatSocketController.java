package louie.hanse.shareplate.web.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;

    @MessageMapping("/chatrooms/{chatRoomId}/chat")
    public void saveChat(@DestinationVariable Long chatRoomId, Map<String, String> map,
        StompHeaderAccessor stompHeaderAccessor) {
        Long memberId = (Long) stompHeaderAccessor.getSessionAttributes().get("memberId");
        String contents = map.get("contents");
        chatService.saveAndResponseChatMessage(chatRoomId, memberId, contents);
    }

}
