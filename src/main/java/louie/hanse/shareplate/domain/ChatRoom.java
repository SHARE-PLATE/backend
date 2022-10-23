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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.ChatExceptionType;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    @Enumerated(EnumType.STRING)
    private ChatRoomType type;

    public ChatRoom(Member member, Share share, ChatRoomType type) {
        this.share = share;
        this.type = type;
        ChatRoomMember chatRoomMember = new ChatRoomMember(member, this);
        chatRoomMembers.add(chatRoomMember);
        share.addChatRoom(this);
    }

    public void addChatRoomMember(Member member) {
        chatRoomMembers.add(new ChatRoomMember(member, this));
        share.addChatRoom(this);
    }

    public boolean isEntry() {
        return type.isEntry();
    }

    public boolean isQuestion() {
        return type.isQuestion();
    }

    public void shareIsCancelThrowException() {
        if (share.isCancel()) {
            new GlobalException(ChatExceptionType.CAN_NOT_WRITE_CHAT);
        }
    }
}
