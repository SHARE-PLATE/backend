package louie.hanse.shareplate.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Entry(Share share, Member member) {
        this.share = share;
        this.share.getEntries().add(this);
        this.member = member;
    }
}
