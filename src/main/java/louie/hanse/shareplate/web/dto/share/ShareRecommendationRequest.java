package louie.hanse.shareplate.web.dto.share;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShareRecommendationRequest {

    private String keyword;
    private double latitude;
    private double longitude;
}
