package louie.hanse.shareplate.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.type.ShareType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShareImage> shareImages = new ArrayList<>();

    @OneToMany(mappedBy = "share")
    private List<Entry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "share")
    private List<Wish> wishList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ShareType type;

    private String title;
    private int price;
    private int originalPrice;
    private int recruitment;
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime createdDateTime = LocalDateTime.now();

    public Share(Long id, Member writer, ShareType type, String title, int price, int originalPrice,
        int recruitment, String location, double latitude, double longitude, String description,
        LocalDateTime appointmentDateTime) {
        this.id = id;
        this.writer = writer;
        this.type = type;
        this.title = title;
        this.price = price;
        this.originalPrice = originalPrice;
        this.recruitment = recruitment;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.appointmentDateTime = appointmentDateTime;
    }

    public Share(Member writer, ShareType type, String title, int price, int originalPrice,
        int recruitment, String location, double latitude, double longitude, String description,
        LocalDateTime appointmentDateTime) {
        this.writer = writer;
        this.type = type;
        this.title = title;
        this.price = price;
        this.originalPrice = originalPrice;
        this.recruitment = recruitment;
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

    public int getCurrentRecruitment() {
        return entries.size() + 1;
    }

}
