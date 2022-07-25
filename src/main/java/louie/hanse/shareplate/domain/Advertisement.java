package louie.hanse.shareplate.domain;

import louie.hanse.shareplate.type.AdvertisementType;

import javax.persistence.*;

@Entity
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Share share;

    @Enumerated(EnumType.STRING)
    private AdvertisementType type;

    private String imageUrl;
}
