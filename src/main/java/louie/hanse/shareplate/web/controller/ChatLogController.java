package louie.hanse.shareplate.web.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ChatLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class ChatLogController {

    private final ChatLogService chatLogService;

    @PutMapping("/chat-logs/update-read-time")
    public void updateRecentReadDateTime(
        @RequestBody Map<String, @Valid @NotNull(message = "요청한 채팅방정보 필드값이 비어있습니다.") @Positive(message = "채팅방 id는 양수여야 합니다.") Long> map,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        Long chatRoomId = map.get("chatRoomId");
        chatLogService.updateRecentReadDateTime(memberId, chatRoomId);
    }
}
