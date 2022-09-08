package louie.hanse.shareplate.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.type.ChatRoomType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chats = new ArrayList<>();

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Share share;

    @Enumerated(EnumType.STRING)
    private ChatRoomType type;

    public ChatRoom(Member member, Share share, ChatRoomType type) {
        ChatRoomMember chatRoomMember = new ChatRoomMember(member, this);
        chatRoomMembers.add(chatRoomMember);
        this.share = share;
        share.changeChatRoom(this);
        this.type = type;
    }

    public void addChatRoomMember(Member member) {
        chatRoomMembers.add(new ChatRoomMember(member, this));
    }
}
