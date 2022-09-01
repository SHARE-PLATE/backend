package louie.hanse.shareplate.web.dto.keyword;

import lombok.Getter;
import louie.hanse.shareplate.domain.Keyword;

@Getter
public class KeywordRegisterResponse {

    private Long id;
    private String keyword;
    private String location;
    private double latitude;
    private double longitude;

    public KeywordRegisterResponse(Keyword keyword) {
        this.id = keyword.getId();
        this.keyword = keyword.getContents();
        this.location = keyword.getLocation();
        this.latitude = keyword.getLatitude();
        this.longitude = keyword.getLongitude();
    }
}
