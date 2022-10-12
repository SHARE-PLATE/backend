package louie.hanse.shareplate.web.dto.share;

import java.time.LocalDateTime;
import lombok.Getter;
import louie.hanse.shareplate.domain.Share;

@Getter
public class SharePreviewResponse {

    private Long id;
    private Long writerId;
    private String thumbnailImageUrl;
    private String title;
    private int price;
    private int originalPrice;
    private boolean cancel;
    private int currentRecruitment;
    private int finalRecruitment;
    private String location;
    private LocalDateTime closedDateTime;
    private String writer;

    public SharePreviewResponse(Share share) {
        this.id = share.getId();
        this.writerId = share.getWriter().getId();
        this.thumbnailImageUrl = share.getShareImages().get(0).getImageUrl();
        this.title = share.getTitle();
        this.price = share.getPrice();
        this.originalPrice = share.getOriginalPrice();
        this.cancel = share.isCancel();
        this.currentRecruitment = share.getCurrentRecruitment();
        this.finalRecruitment = share.getRecruitment();
        this.location = share.getLocation();
        this.closedDateTime = share.getClosedDateTime();
        this.writer = share.getWriter().getNickname();
    }
}
