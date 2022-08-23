package louie.hanse.shareplate.web.dto.share;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;

@Getter
@NoArgsConstructor
public class ShareWriterResponse {

    private String writer;
    private String thumbnailUrl;
    private int shareCount;
    private List<ShareCommonResponse> shares;

    public ShareWriterResponse(Member writer) {
        List<Share> shares = writer.getShares();
        this.writer = writer.getNickname();
        this.thumbnailUrl = writer.getThumbnailImageUrl();
        this.shareCount = shares.size();
        this.shares = shares.stream()
            .map(ShareCommonResponse::new)
            .collect(Collectors.toList());
    }
}
