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
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.EntryExceptionType;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
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

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Entry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hashtag> hashtags = new ArrayList<>();

    @OneToMany(mappedBy = "share", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ShareType type;

    private boolean cancel;
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
    private LocalDateTime closedDateTime;
    private LocalDateTime createdDateTime = LocalDateTime.now();

    public Share(Long id, Member writer, ShareType type, String title, int price, int originalPrice,
        int recruitment, String location, double latitude, double longitude, String description,
        LocalDateTime closedDateTime, String locationGuide, boolean locationNegotiation,
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
        this.closedDateTime = closedDateTime;
        this.locationGuide = locationGuide;
        this.locationNegotiation = locationNegotiation;
        this.priceNegotiation = priceNegotiation;
    }

    public Share(Member writer, ShareType type, String title, int price, int originalPrice,
        int recruitment, String location, double latitude, double longitude, String description,
        LocalDateTime closedDateTime, String locationGuide, boolean locationNegotiation,
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
        this.closedDateTime = closedDateTime;
        this.locationGuide = locationGuide;
        this.locationNegotiation = locationNegotiation;
        this.priceNegotiation = priceNegotiation;
    }

    public void addShareImage(String shareImageUrl) {
        ShareImage shareImage = new ShareImage(this, shareImageUrl);
        shareImages.add(shareImage);
    }

    public void addHashtag(String contents) {
        hashtags.add(new Hashtag(this, contents));
    }

    public void addChatRoom(ChatRoom chatRoom) {
        this.chatRooms.add(chatRoom);
    }

    public boolean isNotEnd() {
        if (closedDateTime.compareTo(LocalDateTime.now()) > 0) {
            return true;
        }
        return false;
    }

    public boolean isEnd() {
        return !isNotEnd();
    }

    public boolean isLeftLessThanAnHour() {
        LocalDateTime leftAnHour = closedDateTime.minusHours(1);
        if (leftAnHour.compareTo(LocalDateTime.now()) < 0) {
            return true;
        }
        return false;
    }

    public void isNotWriterThrowException(Member member) {
        if (isNotWriter(member)) {
            throw new GlobalException(ShareExceptionType.IS_NOT_WRITER);
        }
    }


    public int getCurrentRecruitment() {
        return entries.size();
    }

    public int getWishCount() {
        return wishList.size();
    }

    public ChatRoom getEntryChatRoom() {
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.isEntry()) {
                return chatRoom;
            }
        }
        throw new RuntimeException("참여 채팅방을 찾을 수 없습니다.");
    }

    private boolean isNotWriter(Member member) {
        return !writer.equals(member);
    }

    public void recruitmentQuotaExceededThrowException() {
        if (getCurrentRecruitment() >= getRecruitment()) {
            throw new GlobalException(EntryExceptionType.SHARE_OVERCAPACITY);
        }
    }

    public void cancel() {
        cancel = true;
    }

    public void isClosedThrowException() {
        if (closedDateTime.isBefore(LocalDateTime.now())) {
            throw new GlobalException(ShareExceptionType.SHARE_IS_CLOSED);
        }
    }

    public void isCanceledThrowException() {
        if (cancel) {
            throw new GlobalException(ShareExceptionType.SHARE_IS_CANCELED);
        }
    }
}
