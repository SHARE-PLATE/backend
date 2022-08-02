package louie.hanse.shareplate.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ShareImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    private String imageUrl;

    public ShareImage(Share share, String imageUrl) {
        this.share = share;
        this.imageUrl = imageUrl;
    }
}
