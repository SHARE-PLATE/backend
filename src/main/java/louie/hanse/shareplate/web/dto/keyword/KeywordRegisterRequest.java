package louie.hanse.shareplate.web.dto.keyword;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import louie.hanse.shareplate.domain.Keyword;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.validator.keyword.ValidKeywordLatitude;
import louie.hanse.shareplate.validator.keyword.ValidKeywordLongitude;

@Getter
public class KeywordRegisterRequest {

    @NotBlank(message = "요청한 키워드정보 필드값이 비어있습니다.")
    private String keyword;

    @NotBlank(message = "요청한 키워드정보 필드값이 비어있습니다.")
    private String location;

    @ValidKeywordLatitude
    private Double latitude;

    @ValidKeywordLongitude
    private Double longitude;

    public Keyword toEntity(Member member) {
        return new Keyword(member, keyword, location, latitude, longitude);
    }
}
