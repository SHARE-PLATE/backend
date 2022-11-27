package louie.hanse.shareplate.repository;

import static com.querydsl.core.types.dsl.Expressions.constant;
import static com.querydsl.core.types.dsl.MathExpressions.acos;
import static com.querydsl.core.types.dsl.MathExpressions.cos;
import static com.querydsl.core.types.dsl.MathExpressions.radians;
import static com.querydsl.core.types.dsl.MathExpressions.sin;
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
import louie.hanse.shareplate.web.dto.share.QShareRecommendationResponse;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationRequest;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationResponse;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class CustomShareRepositoryImpl implements CustomShareRepository {

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
                share.cancel.eq(false),
                share.closedDateTime.gt(LocalDateTime.now()),
                acos(cos(radians(constant(latitude)))
                    .multiply(cos(radians(share.latitude)))
                    .multiply(cos(radians(share.longitude).subtract(radians(constant(longitude)))))
                    .add(sin(radians(constant(latitude))).multiply(sin(radians(share.latitude)))))
                    .multiply(constant(6371))
                    .loe(2)
            ).fetch();
    }

    @Override
    public List<ShareRecommendationResponse> recommendationAroundMember(
        ShareRecommendationRequest request) {
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        return queryFactory.select(new QShareRecommendationResponse(share))
            .from(share)
            .where(
                titleContains(request.getKeyword()),
                share.cancel.eq(false),
                share.closedDateTime.gt(LocalDateTime.now()),
                acos(cos(radians(constant(latitude)))
                    .multiply(cos(radians(share.latitude)))
                    .multiply(cos(radians(share.longitude).subtract(radians(constant(longitude)))))
                    .add(sin(radians(constant(latitude))).multiply(sin(radians(share.latitude)))))
                    .multiply(constant(6371))
                    .loe(2)
            )
            .fetch();
    }

    @Override
    public List<Share> findByWriterIdAndTypeAndIsExpired(
        Long writerId, ShareType type, boolean expired, LocalDateTime currentDateTime) {
        return queryFactory
            .selectFrom(share)
            .where(
                typeEq(type),
                share.writer.id.eq(writerId),
                isExpired(expired, currentDateTime)
            ).fetch();
    }

    @Override
    public List<Share> findWithEntryByMemberIdAndTypeAndNotWriteByMeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime) {
        return queryFactory
            .selectFrom(share)
            .join(share.entries, entry).on(entry.member.id.eq(memberId))
            .where(
                share.writer.id.ne(memberId),
                typeEq(type),
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
                typeEq(type),
                isExpired(expired, currentDateTime)
            ).fetch();
    }

    private BooleanExpression typeEq(ShareType type) {
        return ObjectUtils.isEmpty(type) ? null : share.type.eq(type);
    }

    private BooleanExpression isExpired(boolean expired, LocalDateTime currentDateTime) {
        if (expired) {
            return share.closedDateTime.lt(currentDateTime);
        }
        return share.closedDateTime.gt(currentDateTime);
    }

    private BooleanExpression titleContains(String keyword) {
        return StringUtils.hasText(keyword) ? share.title.contains(keyword) : null;
    }

}
