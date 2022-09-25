package louie.hanse.shareplate.web.dto.share;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.validator.share.ValidShareLatitude;
import louie.hanse.shareplate.validator.share.ValidShareLongitude;

@Setter
@Getter
public class ShareSearchRequest {
    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private ShareType type;
    @NotBlank(message = "요청한 쉐어정보 값이 비어있습니다.")
    private String keyword;

    @ValidShareLatitude
    private Double latitude;
    @ValidShareLongitude
    private Double longitude;
}
