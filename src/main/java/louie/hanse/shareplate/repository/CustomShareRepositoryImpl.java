package louie.hanse.shareplate.repository;

import static louie.hanse.shareplate.domain.QShare.share;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class CustomShareRepositoryImpl implements CustomShareRepository {

    private static final int SEARCH_RANGE = 2;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Share> searchAroundMember(ShareSearchRequest request) {
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        return queryFactory
            .selectFrom(share)
            .where(
                share.type.eq(request.getType()),
                titleContains(request.getKeyword()),
                share.latitude.between(calculateStartLatitude(latitude), calculateEndLatitude(latitude)),
                share.longitude.between(calculateStartLongitude(longitude), calculateEndLongitude(longitude))
            ).fetch();
    }

    private BooleanExpression titleContains(String keyword) {
        return StringUtils.hasText(keyword) ? share.title.contains(keyword) : null;
    }

    private double calculateStartLatitude(double latitude) {
        return latitude - SEARCH_RANGE;
    }

    private double calculateEndLatitude(double latitude) {
        return latitude + SEARCH_RANGE;
    }

    private double calculateStartLongitude(double longitude) {
        return longitude - SEARCH_RANGE;
    }

    private double calculateEndLongitude(double longitude) {
        return longitude + SEARCH_RANGE;
    }
}
