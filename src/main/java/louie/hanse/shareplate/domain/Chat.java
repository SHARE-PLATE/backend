package louie.hanse.shareplate.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    private String contents;

    private LocalDateTime writtenDateTime = LocalDateTime.now();

    public Chat(ChatRoom chatRoom, Member writer, String contents) {
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.contents = contents;
    }
}
