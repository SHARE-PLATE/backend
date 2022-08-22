package louie.hanse.shareplate.web.dto.share;


import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.domain.Share;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShareRecommendationResponse {

    private Long id;
    private String thumbnailUrl;
    private String title;
    private String location;
    private int price;
    private LocalDateTime createdDateTime;
    private LocalDateTime appointmentDateTime;

    @QueryProjection
    public ShareRecommendationResponse(Share share) {
        this.id = share.getId();
        this.thumbnailUrl = share.getShareImages().get(0).getImageUrl();
        this.title = share.getTitle();
        this.location = share.getLocation();
        this.price = share.getPrice();
        this.createdDateTime = share.getCreatedDateTime();
        this.appointmentDateTime = share.getAppointmentDateTime();
    }
}
