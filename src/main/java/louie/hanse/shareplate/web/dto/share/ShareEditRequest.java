package louie.hanse.shareplate.web.dto.share;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Setter;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.type.ShareType;
import org.springframework.web.multipart.MultipartFile;

@Setter
public class ShareEditRequest {

    private ShareType type;
    private List<MultipartFile> images;
    private String title;
    private int price;
    private int originalPrice;
    private int recruitment;
    private String locationGuide;
    private boolean negotiation;
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private List<String> hashtags;
    private LocalDateTime appointmentDateTime;

    public Share toEntity(Long id, Member member) {
        return new Share(id, member, type, title, price, originalPrice, recruitment, location,
            latitude, longitude, description, appointmentDateTime, locationGuide, negotiation);
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public List<String> getHashtags() {
        return hashtags;
    }
}
