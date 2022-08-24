package louie.hanse.shareplate.web.controller;

import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/unread")
    public Map<String, Integer> getUnread(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        int count = chatService.getTotalUnread(memberId);
        return Collections.singletonMap("count", count);
    }
}
