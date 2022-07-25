package louie.hanse.shareplate.domain;

import javax.persistence.*;

@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
