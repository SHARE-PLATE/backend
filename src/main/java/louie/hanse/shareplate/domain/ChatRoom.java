package louie.hanse.shareplate.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Share share;

    public ChatRoom(Member member, Share share) {
        ChatRoomMember chatRoomMember = new ChatRoomMember(member, this);
        chatRoomMembers.add(chatRoomMember);
        this.share = share;
        share.changeChatRoom(this);
    }
}
