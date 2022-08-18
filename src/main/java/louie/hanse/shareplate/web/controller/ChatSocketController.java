package louie.hanse.shareplate.web.controller;

import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Chat;
import louie.hanse.shareplate.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;

    @MessageMapping("/chatrooms/{chatRoomId}/chat")
    @SendTo("/topic/chatrooms/{chatRoomId}")
    public Map<String, String> saveChat(@DestinationVariable Long chatRoomId, Map<String, String> map,
        StompHeaderAccessor stompHeaderAccessor) {
        Long memberId = (Long) stompHeaderAccessor.getSessionAttributes().get("memberId");
        String contents = map.get("contents");
        Chat chat = chatService.save(chatRoomId, memberId, contents);
        return Collections.singletonMap("contents", chat.getContents());
    }

}
