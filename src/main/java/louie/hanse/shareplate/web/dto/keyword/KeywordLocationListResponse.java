package louie.hanse.shareplate.web.dto.keyword;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.domain.Keyword;

@Getter
public class KeywordLocationListResponse {

    private double longitude;
    private double latitude;
    private List<KeywordDetailResponse> keywords;

    @QueryProjection
    public KeywordLocationListResponse(List<Keyword> keywords) {
        this.longitude = keywords.get(0).getLongitude();
        this.latitude = keywords.get(0).getLatitude();
        this.keywords = keywords.stream()
            .map(KeywordDetailResponse::new)
            .collect(Collectors.toList());
    }

}
