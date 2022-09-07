package louie.hanse.shareplate.web.dto.share;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Setter;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.type.ShareType;
import org.springframework.web.multipart.MultipartFile;

@Setter
public class ShareRegisterRequest {

    private ShareType type;
    private List<MultipartFile> images;
    private String title;
    private int price;
    private int originalPrice;
    private int recruitment;
    private String location;
    private String locationGuide;
    private boolean locationNegotiation;
    private boolean priceNegotiation;
    private List<String> hashtags;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime closedDateTime;

    public Share toEntity(Member member) {
        return new Share(member, type, title, price, originalPrice, recruitment, location, latitude,
            longitude, description, closedDateTime, locationGuide, locationNegotiation,
            priceNegotiation);
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public List<String> getHashtags() {
        return hashtags;
    }
}
