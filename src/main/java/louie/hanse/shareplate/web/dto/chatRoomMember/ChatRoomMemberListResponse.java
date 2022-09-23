package louie.hanse.shareplate.web.dto.chatRoomMember;

import lombok.Getter;
import louie.hanse.shareplate.domain.ChatRoomMember;

@Getter
public class ChatRoomMemberListResponse {
    private Long id;
    private Long chatRoomId;

    public ChatRoomMemberListResponse(ChatRoomMember chatRoomMember) {
        this.id = chatRoomMember.getId();
        this.chatRoomId = chatRoomMember.getChatRoom().getId();
    }
}
