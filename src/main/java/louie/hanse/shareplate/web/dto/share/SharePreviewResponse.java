package louie.hanse.shareplate.web.dto.share;

import lombok.Getter;
import louie.hanse.shareplate.domain.Share;

@Getter
public class SharePreviewResponse {

    private Long id;
    private String thumbnailImageUrl;
    private String title;
    private int price;
    private int originalPrice;
    private int currentRecruitment;
    private int finalRecruitment;

    public SharePreviewResponse(Share share) {
        this.id = share.getId();
        this.thumbnailImageUrl = share.getShareImages().get(0).getImageUrl();
        this.title = share.getTitle();
        this.price = share.getPrice();
        this.originalPrice = share.getOriginalPrice();
        this.currentRecruitment = share.getCurrentRecruitment();
        this.finalRecruitment = share.getRecruitment();
    }
}
