package louie.hanse.shareplate.web.dto.keyword;

import lombok.Getter;
import louie.hanse.shareplate.domain.Keyword;
import louie.hanse.shareplate.domain.Member;

@Getter
public class KeywordRegisterRequest {

    private String keyword;
    private String location;
    private double latitude;
    private double longitude;

    public Keyword toEntity(Member member) {
        return new Keyword(member, keyword, location, latitude, longitude);
    }
}
