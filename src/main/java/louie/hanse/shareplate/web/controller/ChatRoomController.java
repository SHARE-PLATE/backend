package louie.hanse.shareplate.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ChatRoomService;
import louie.hanse.shareplate.web.dto.chatroom.ChatRoomDetailResponse;
import louie.hanse.shareplate.web.dto.chatroom.ChatRoomListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chatrooms/{id}")
    public ChatRoomDetailResponse chatRoomDetail(@PathVariable Long id,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return chatRoomService.getDetail(id, memberId);
    }

    @GetMapping("/chatrooms")
    public List<ChatRoomListResponse> chatRoomList(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return chatRoomService.getList(memberId);
    }

}
