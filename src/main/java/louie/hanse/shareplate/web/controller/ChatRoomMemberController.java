package louie.hanse.shareplate.web.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ChatRoomMemberService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chatroom-members")
@RestController
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @DeleteMapping
    public void exitChatRoom(@RequestBody Map<String, Long> map, HttpServletRequest request) {
        Long chatRoomId = map.get("chatRoomId");
        Long memberId = (Long) request.getAttribute("memberId");
        chatRoomMemberService.exitChatRoom(chatRoomId, memberId);
    }
}
