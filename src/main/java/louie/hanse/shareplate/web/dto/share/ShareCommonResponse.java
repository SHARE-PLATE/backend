package louie.hanse.shareplate.web.dto.share;


import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import louie.hanse.shareplate.domain.Share;

@Getter
public class ShareCommonResponse {

    private Long id;
    private String thumbnailUrl;
    private String title;
    private String location;
    private int price;
    private int currentRecruitment;
    private int finalRecruitment;
    private LocalDateTime createdDateTime;
    private LocalDateTime closedDateTime;

    @QueryProjection
    public ShareCommonResponse(Share share) {
        this.id = share.getId();
        this.thumbnailUrl = share.getShareImages().get(0).getImageUrl();
        this.title = share.getTitle();
        this.location = share.getLocation();
        this.price = share.getPrice();
        this.currentRecruitment = share.getCurrentRecruitment();
        this.finalRecruitment = share.getRecruitment();
        this.createdDateTime = share.getCreatedDateTime();
        this.closedDateTime = share.getClosedDateTime();
    }
}
