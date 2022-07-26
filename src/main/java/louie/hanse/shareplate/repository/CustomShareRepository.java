package louie.hanse.shareplate.repository;

import java.time.LocalDateTime;
import java.util.List;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationResponse;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;

public interface CustomShareRepository {

    List<Share> searchAroundMember(ShareSearchRequest request);

    List<ShareRecommendationResponse> recommendationAroundMember(ShareRecommendationRequest request);

    List<Share> findByWriterIdAndTypeAndIsExpired(
        Long writerId, ShareType type, boolean expired, LocalDateTime currentDateTime);

    List<Share> findWithEntryByMemberIdAndTypeAndNotWriteByMeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime);

    List<Share> findWithWishByMemberIdAndTypeAndIsExpired(
        Long memberId, ShareType type, boolean expired, LocalDateTime currentDateTime);

}

