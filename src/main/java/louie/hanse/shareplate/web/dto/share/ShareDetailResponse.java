package louie.hanse.shareplate.web.dto.share;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.domain.Entry;
import louie.hanse.shareplate.domain.Hashtag;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.domain.ShareImage;

@Getter
public class ShareDetailResponse {

    private Long id;
    private List<String> imageUrls;
    private String writer;
    private Long writerId;
    private String writerThumbnailImageUrl;
    private String title;
    private List<String> hashtags;
    private boolean locationNegotiation;
    private boolean priceNegotiation;
    private String locationGuide;
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private int price;
    private int originalPrice;
    private int currentRecruitment;
    private int finalRecruitment;
    private List<String> recruitmentMemberThumbnailImageUrls;
    private LocalDateTime createdDateTime;
    private LocalDateTime closedDateTime;
    private boolean wish;
    private boolean entry;

    @JsonProperty("isWriter")
    private boolean isWriter;
    private int wishCount;

    public ShareDetailResponse(Share share, Member member, boolean wish, boolean entry) {
        this.id = share.getId();
        this.imageUrls = share.getShareImages().stream()
            .map(ShareImage::getImageUrl)
            .collect(Collectors.toList());
        this.writer = share.getWriter().getNickname();
        this.writerId = share.getWriter().getId();
        this.writerThumbnailImageUrl = share.getWriter().getThumbnailImageUrl();
        this.title = share.getTitle();
        this.location = share.getLocation();
        this.latitude = share.getLatitude();
        this.longitude = share.getLongitude();
        this.description = share.getDescription();
        this.price = share.getPrice();
        this.originalPrice = share.getOriginalPrice();
        this.currentRecruitment = share.getCurrentRecruitment();
        this.finalRecruitment = share.getRecruitment();
        this.recruitmentMemberThumbnailImageUrls = share.getEntries().stream()
            .map(Entry::getMember)
            .map(Member::getThumbnailImageUrl)
            .collect(Collectors.toList());
        this.createdDateTime = share.getCreatedDateTime();
        this.closedDateTime = share.getClosedDateTime();
        this.wish = wish;
        this.entry = entry;
        this.isWriter = share.isWriter(member);
        this.wishCount = share.getWishCount();
        this.locationNegotiation = share.isLocationNegotiation();
        this.priceNegotiation = share.isPriceNegotiation();
        this.hashtags = share.getHashtags().stream()
            .map(Hashtag::getContents)
            .collect(Collectors.toList());
        this.locationGuide = share.getLocationGuide();
    }
}
