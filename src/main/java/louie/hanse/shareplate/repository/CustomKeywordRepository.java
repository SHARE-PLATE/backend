package louie.hanse.shareplate.repository;

import java.util.List;
import louie.hanse.shareplate.web.dto.keyword.KeywordListResponse;

public interface CustomKeywordRepository {

    List<KeywordListResponse> getKeywords(Long memberId);
}
