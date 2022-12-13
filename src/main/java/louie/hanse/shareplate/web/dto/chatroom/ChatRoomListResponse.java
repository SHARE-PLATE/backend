package louie.hanse.shareplate.web.dto.chatroom;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.domain.Chat;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.ChatRoomMember;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;

@Getter
public class ChatRoomListResponse {

    private Long id;
    private Long chatRoomMemberId;
    private String shareThumbnailImageUrl;
    private int currentRecruitment;
    private boolean cancel;
    private String recentMessage;
    private LocalDateTime recentMessageDataTime;
    private List<String> recruitmentMemberNicknames;
    private List<String> recruitmentMemberImageUrls;
    private int unreadCount;

    public ChatRoomListResponse(ChatRoomMember chatRoomMember, int unreadCount, Long memberId,
        Optional<Chat> optionalChat) {
        ChatRoom chatRoom = chatRoomMember.getChatRoom();
        this.id = chatRoom.getId();
        this.chatRoomMemberId = chatRoomMember.getId();
        this.shareThumbnailImageUrl = chatRoom.getShare().getShareImages().get(0).getImageUrl();
        this.currentRecruitment = chatRoom.getShare().getCurrentRecruitment();
        this.cancel = chatRoom.getShare().isCancel();
        this.recruitmentMemberNicknames = chatRoom.getChatRoomMembers().stream()
            .map(ChatRoomMember::getMember)
            .filter(member -> !member.getId().equals(memberId))
            .map(Member::getNickname).collect(Collectors.toList());
        this.recruitmentMemberImageUrls = chatRoom.getChatRoomMembers().stream()
            .map(ChatRoomMember::getMember)
            .filter(member -> !member.getId().equals(memberId))
            .map(Member::getThumbnailImageUrl).collect(Collectors.toList());
        this.unreadCount = unreadCount;

        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            this.recentMessage = chat.getContents();
            this.recentMessageDataTime = chat.getWrittenDateTime();
        }
    }

    public ChatRoomListResponse(ChatRoomMember chatRoomMember, int unreadCount,
        Optional<Chat> optionalChat) {
        ChatRoom chatRoom = chatRoomMember.getChatRoom();
        Share share = chatRoom.getShare();
        Member writer = share.getWriter();

        this.id = chatRoom.getId();
        this.chatRoomMemberId = chatRoomMember.getId();
        this.shareThumbnailImageUrl = share.getShareImages().get(0).getImageUrl();
        this.currentRecruitment = share.getCurrentRecruitment();
        this.cancel = share.isCancel();
        this.recruitmentMemberNicknames = List.of(writer.getNickname());
        this.recruitmentMemberImageUrls = List.of(writer.getThumbnailImageUrl());
        this.unreadCount = unreadCount;

        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            this.recentMessage = chat.getContents();
            this.recentMessageDataTime = chat.getWrittenDateTime();
        }
    }
}
