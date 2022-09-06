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
import javax.persistence.OneToOne;
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

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ShareImage> shareImages = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Entry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Hashtag> hashtags = new ArrayList<>();

    @OneToOne(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    private ShareType type;

    private String title;
    private int price;
    private int originalPrice;
    private int recruitment;
    private boolean locationNegotiation;
    private boolean priceNegotiation;
    private String locationGuide;
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime appointmentDateTime;
    private LocalDateTime createdDateTime = LocalDateTime.now();

    public Share(Long id, Member writer, ShareType type, String title, int price, int originalPrice,
        int recruitment, String location, double latitude, double longitude, String description,
        LocalDateTime appointmentDateTime, String locationGuide, boolean locationNegotiation,
        boolean priceNegotiation) {
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
        this.locationGuide = locationGuide;
        this.locationNegotiation = locationNegotiation;
        this.priceNegotiation = priceNegotiation;
    }

    public Share(Member writer, ShareType type, String title, int price, int originalPrice,
        int recruitment, String location, double latitude, double longitude, String description,
        LocalDateTime appointmentDateTime, String locationGuide, boolean locationNegotiation,
        boolean priceNegotiation) {
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
        this.locationGuide = locationGuide;
        this.locationNegotiation = locationNegotiation;
        this.priceNegotiation = priceNegotiation;
    }

    public void addShareImage(String shareImageUrl) {
        ShareImage shareImage = new ShareImage(this, shareImageUrl);
        shareImages.add(shareImage);
    }

    public void changeChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public boolean isNotEnd() {
        if (appointmentDateTime.compareTo(LocalDateTime.now()) > 0) {
            return true;
        }
        return false;
    }

    public int getCurrentRecruitment() {
        return entries.size();
    }

    public int getWishCount() {
        return wishList.size();
    }
}
