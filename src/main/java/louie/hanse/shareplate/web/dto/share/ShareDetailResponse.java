package louie.hanse.shareplate.web.dto.share;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.domain.Entry;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.domain.ShareImage;

@Getter
public class ShareDetailResponse {

    private Long id;
    private List<String> imageUrls;
    private String writer;
    private String writerThumbnailImageUrl;
    private String title;
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
    private LocalDateTime appointmentDateTime;
    private boolean wish;
    private boolean entry;
    private int wishCount;

    public ShareDetailResponse(Share share, boolean wish, boolean entry) {
        this.id = share.getId();
        this.imageUrls = share.getShareImages().stream()
            .map(ShareImage::getImageUrl)
            .collect(Collectors.toList());
        this.writer = share.getWriter().getNickname();
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
        this.appointmentDateTime = share.getAppointmentDateTime();
        this.wish = wish;
        this.entry = entry;
        this.wishCount = share.getWishCount();
    }
}
