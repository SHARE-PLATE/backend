package louie.hanse.shareplate.repository;

import static louie.hanse.shareplate.domain.QEntry.entry;
import static louie.hanse.shareplate.domain.QShare.share;
import static louie.hanse.shareplate.domain.QWish.wish;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.web.dto.share.QShareCommonResponse;
import louie.hanse.shareplate.web.dto.share.ShareCommonResponse;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import org.springframework.util.ObjectUtils;
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
                typeEq(request.getType()),
                titleContains(request.getKeyword()),
                share.latitude.between(calculateStartLatitude(latitude),
                    calculateEndLatitude(latitude)),
                share.longitude.between(calculateStartLongitude(longitude),
                    calculateEndLongitude(longitude))
            ).fetch();
    }

    @Override
    public List<ShareCommonResponse> recommendationAroundMember(
        ShareRecommendationRequest request) {
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        return queryFactory.select(new QShareCommonResponse(share))
            .from(share)
            .where(
                titleContains(request.getKeyword()),
                share.latitude.between(calculateStartLatitude(latitude),
                    calculateEndLatitude(latitude)),
                share.longitude.between(calculateStartLongitude(longitude),
                    calculateEndLongitude(longitude))
            )
            .fetch();
    }

    @Override
    public List<Share> findByWriterIdAndTypeAndIsExpired(
        Long writerId, ShareType type, boolean expired, LocalDateTime currentDateTime) {
        return queryFactory
            .selectFrom(share)
            .where(
                share.type.eq(type),
                share.writer.id.eq(writerId),
                isExpired(expired, currentDateTime)
            ).fetch();
    }

    @Override
    public List<Share> findWithEntryByMemberIdAndTypeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime) {
        return queryFactory
            .selectFrom(share)
            .join(share.entries, entry).on(entry.member.id.eq(memberId))
            .where(
                share.type.eq(type),
                isExpired(expired, currentDateTime)
            ).fetch();
    }

    @Override
    public List<Share> findWithWishByMemberIdAndTypeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime) {
        return queryFactory
            .selectFrom(share)
            .join(share.wishList, wish).on(wish.member.id.eq(memberId))
            .where(
                share.type.eq(type),
                isExpired(expired, currentDateTime)
            ).fetch();
    }

    private BooleanExpression typeEq(ShareType type) {
        return ObjectUtils.isEmpty(type) ? null : share.type.eq(type);
    }

    private BooleanExpression isExpired(boolean expired, LocalDateTime currentDateTime) {
        if (expired) {
            return share.appointmentDateTime.lt(currentDateTime);
        }
        return share.appointmentDateTime.gt(currentDateTime);
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
