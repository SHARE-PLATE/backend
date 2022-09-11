package louie.hanse.shareplate.web.dto.keyword;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.domain.Keyword;

@Getter
public class KeywordListResponse {

    private String location;
    private List<KeywordDetailResponse> keywords;

    @QueryProjection
    public KeywordListResponse(String location, List<Keyword> keywords) {
        this.location = location;
        this.keywords = keywords.stream()
            .map(KeywordDetailResponse::new)
            .collect(Collectors.toList());
    }

}
