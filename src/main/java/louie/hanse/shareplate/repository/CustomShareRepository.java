package louie.hanse.shareplate.repository;

import java.util.List;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.web.dto.share.ShareCommonResponse;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;

public interface CustomShareRepository {

    List<Share> searchAroundMember(ShareSearchRequest request);

    List<ShareCommonResponse> recommendationAroundMember(ShareRecommendationRequest request);

}
