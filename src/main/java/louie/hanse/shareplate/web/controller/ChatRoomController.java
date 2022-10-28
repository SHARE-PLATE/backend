package louie.hanse.shareplate.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.ChatRoomService;
import louie.hanse.shareplate.type.ChatRoomType;
import louie.hanse.shareplate.web.dto.chatroom.ChatRoomDetailResponse;
import louie.hanse.shareplate.web.dto.chatroom.ChatRoomListResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
@Validated
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/{id}")
    public ChatRoomDetailResponse chatRoomDetail(
        @PathVariable(required = false) @NotNull(message = "PathVariable의 ChatroomId가 비어있습니다.") @Positive(message = "채팅방 id는 양수여야 합니다.") Long id,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return chatRoomService.getDetail(id, memberId);
    }

    @GetMapping
    public List<ChatRoomListResponse> chatRoomList(HttpServletRequest request,
        @Valid @RequestParam(required = false) @NotNull(message = "요청한 채팅방정보 필드값이 비어있습니다.") ChatRoomType type) {
        Long memberId = (Long) request.getAttribute("memberId");
        return chatRoomService.getList(memberId, type);
    }

    @PostMapping
    public Map<String, Long> createQuestionChatRoom(
        @RequestBody Map<String, @Valid @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.") @Positive(message = "쉐어 id는 양수여야 합니다.") Long> map,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        Long shareId = map.get("shareId");
        Long id = chatRoomService.createQuestionChatRoom(memberId, shareId);
        return Collections.singletonMap("id", id);
    }

}
