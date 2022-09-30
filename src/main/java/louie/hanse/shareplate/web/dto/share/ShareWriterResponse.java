package louie.hanse.shareplate.web.dto.share;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;

@Getter
public class ShareWriterResponse {

    private String writer;
    private String thumbnailUrl;
    private int shareCount;
    private List<ShareRecommendationResponse> shares;

    public ShareWriterResponse(Member writer) {
        List<Share> shares = writer.getShares();
        this.writer = writer.getNickname();
        this.thumbnailUrl = writer.getThumbnailImageUrl();
        this.shareCount = shares.size();
        this.shares = shares.stream()
            .map(ShareRecommendationResponse::new)
            .collect(Collectors.toList());
    }
}
