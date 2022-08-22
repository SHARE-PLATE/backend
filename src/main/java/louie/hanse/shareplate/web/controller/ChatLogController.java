package louie.hanse.shareplate.web.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ChatLogService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatLogController {

    private final ChatLogService chatLogService;

    @PutMapping("/chat-logs/update-read-time")
    public void updateRecentReadDateTime(@RequestBody Map<String, Long> map,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        Long chatRoomId = map.get("chatRoomId");
        chatLogService.updateRecentReadDateTime(memberId, chatRoomId);
    }
}
