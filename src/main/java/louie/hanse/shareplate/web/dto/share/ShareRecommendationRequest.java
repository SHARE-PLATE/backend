package louie.hanse.shareplate.web.dto.share;

import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.validator.share.ValidShareLatitude;
import louie.hanse.shareplate.validator.share.ValidShareLongitude;

@Setter
@Getter
public class ShareRecommendationRequest {

    private String keyword;

    @ValidShareLatitude
    private Double latitude;

    @ValidShareLongitude
    private Double longitude;
}
