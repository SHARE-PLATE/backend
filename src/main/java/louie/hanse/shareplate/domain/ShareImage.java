package louie.hanse.shareplate.domain;

import javax.persistence.*;

@Entity
public class ShareImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    private String imageUrl;
}
