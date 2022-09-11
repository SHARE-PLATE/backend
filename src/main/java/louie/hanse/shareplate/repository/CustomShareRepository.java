package louie.hanse.shareplate.repository;

import java.time.LocalDateTime;
import java.util.List;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.web.dto.share.ShareCommonResponse;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;

public interface CustomShareRepository {

    List<Share> searchAroundMember(ShareSearchRequest request);

    List<ShareCommonResponse> recommendationAroundMember(ShareRecommendationRequest request);

    List<Share> findByWriterIdAndTypeAndIsExpired(
        Member writer, ShareType type, boolean expired, LocalDateTime currentDateTime);

    List<Share> findWithEntryByMemberIdAndTypeAndNotWriteByMeAndIsExpired(
        Member member, ShareType type, boolean expired, LocalDateTime currentDateTime);

    List<Share> findWithWishByMemberIdAndTypeAndIsExpired(
        Member member, ShareType type, boolean expired, LocalDateTime currentDateTime);

}

