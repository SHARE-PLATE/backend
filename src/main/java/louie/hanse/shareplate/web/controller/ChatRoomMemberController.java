package louie.hanse.shareplate.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ChatRoomMemberService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chatroom-members")
@RestController
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @GetMapping
    public Map<String, List<Long>> getIdList(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        List<Long> idList = chatRoomMemberService.getIdList(memberId);
        return Collections.singletonMap("idList", idList);
    }

    @DeleteMapping
    public void exitChatRoom(@RequestBody Map<String, Long> map, HttpServletRequest request) {
        Long chatRoomId = map.get("chatRoomId");
        Long memberId = (Long) request.getAttribute("memberId");
        chatRoomMemberService.exitChatRoom(chatRoomId, memberId);
    }
}
