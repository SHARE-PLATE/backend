package louie.hanse.shareplate.domain;

import javax.persistence.*;

@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
