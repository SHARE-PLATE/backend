package louie.hanse.shareplate.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.type.ShareType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShareImage> shareImages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ShareType type;

    private String title;
    private int price;
    private int originalPrice;
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime createDateTime = LocalDateTime.now();

    public Share(Member member, ShareType type, String title, int price, int originalPrice,
        String location, double latitude, double longitude, String description,
        LocalDateTime appointmentDateTime) {
        this.member = member;
        this.type = type;
        this.title = title;
        this.price = price;
        this.originalPrice = originalPrice;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.appointmentDateTime = appointmentDateTime;
    }

    public void addShareImage(String shareImageUrl) {
        ShareImage shareImage = new ShareImage(this, shareImageUrl);
        shareImages.add(shareImage);
    }
}
