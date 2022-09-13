package louie.hanse.shareplate.repository;

import static louie.hanse.shareplate.domain.QKeyword.keyword;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Keyword;
import louie.hanse.shareplate.web.dto.keyword.KeywordListResponse;

@RequiredArgsConstructor
public class CustomKeywordRepositoryImpl implements CustomKeywordRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<KeywordListResponse> getKeywords(Long memberId) {
        List<String> locations = queryFactory
            .select(keyword.location).distinct()
            .from(keyword)
            .where(keyword.member.id.eq(memberId))
            .fetch();

        List<KeywordListResponse> keywordListResponses = new ArrayList<>();
        for (String location : locations) {
            List<Keyword> keywords = queryFactory
                .selectFrom(keyword)
                .where(
                    keyword.location.eq(location),
                    keyword.member.id.eq(memberId)
                )
                .fetch();
            keywordListResponses.add(new KeywordListResponse(location, keywords));
        }

        return keywordListResponses;
    }
}
