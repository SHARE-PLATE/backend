package louie.hanse.shareplate.repository;

import static louie.hanse.shareplate.domain.QShare.share;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class CustomShareRepositoryImpl implements CustomShareRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Share> searchAroundMember(Member member, ShareSearchRequest request) {
        return queryFactory
            .selectFrom(share)
            .where(
                titleContains(request.getKeyword()),
                share.type.eq(request.getType()).and(
                    share.latitude.between(
                        member.calculateStartLatitude(),
                        member.calculateEndLatitude()).and(
                        share.longitude.between(
                            member.calculateStartLongitude(),
                            member.calculateEndLongitude())))
            ).fetch();
    }

//    @Override
//    public List<Share> searchAroundMember(Member member, ShareSearchRequest request) {
//        return queryFactory
//            .selectFrom(share)
//            .where(
//                share.type.eq(request.getType()).and(
//                    share.latitude.between(member.getLatitude() - 2, member.getLatitude() + 2).and(
//                        share.longitude.between(member.getLongitude() - 2, member.getLongitude() + 2))),
//                titleContains(request.getKeyword())
//            ).fetch();
//    }

    private BooleanExpression titleContains(String keyword) {
        return StringUtils.hasText(keyword) ? share.title.contains(keyword) : null;
    }
}
