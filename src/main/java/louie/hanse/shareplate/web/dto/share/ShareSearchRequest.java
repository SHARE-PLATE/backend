package louie.hanse.shareplate.web.dto.share;

import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.type.ShareType;

@Setter
@Getter
public class ShareSearchRequest {
    private ShareType type;
    private String keyword;
    private double latitude;
    private double longitude;
}
