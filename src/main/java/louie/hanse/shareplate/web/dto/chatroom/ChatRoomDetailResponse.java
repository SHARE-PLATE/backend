package louie.hanse.shareplate.web.dto.chatroom;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.ChatRoomMember;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.web.dto.chat.ChatDetailResponse;
import louie.hanse.shareplate.web.dto.share.SharePreviewResponse;

@Getter
public class ChatRoomDetailResponse {

    private Long chatRoomMemberId;
    private SharePreviewResponse share;
    private List<ChatDetailResponse> chats;

    public ChatRoomDetailResponse(ChatRoomMember chatRoomMember, Member member) {
        ChatRoom chatRoom = chatRoomMember.getChatRoom();
        this.chatRoomMemberId = chatRoomMember.getId();
        this.share = new SharePreviewResponse(chatRoom.getShare());
        this.chats = chatRoom.getChats().stream()
            .map(chat -> new ChatDetailResponse(chat, member))
            .collect(Collectors.toList());
    }
}

