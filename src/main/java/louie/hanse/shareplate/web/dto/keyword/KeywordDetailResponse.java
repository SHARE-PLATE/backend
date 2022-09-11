package louie.hanse.shareplate.web.dto.keyword;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import louie.hanse.shareplate.domain.Keyword;

@Getter
public class KeywordDetailResponse {
    private Long id;
    private String contents;

    @QueryProjection
    public KeywordDetailResponse(Keyword keyword) {
        this.id = keyword.getId();
        this.contents = keyword.getContents();
    }
}
