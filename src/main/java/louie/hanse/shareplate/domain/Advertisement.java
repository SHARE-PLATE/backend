package louie.hanse.shareplate.domain;

import louie.hanse.shareplate.type.AdvertisementType;
import louie.hanse.shareplate.type.ShareType;

import javax.persistence.*;

@Entity
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AdvertisementType type;

    private ShareType shareType;
    private String keyword;
    private String imageUrl;
}
