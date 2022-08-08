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
    private boolean recruitmentLimit;
    private String location;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime appointmentDateTime;

    public Share toEntity(Member member) {
        return new Share(member, type, title, price, originalPrice, recruitment, recruitmentLimit, location, latitude, longitude, description, appointmentDateTime);
    }

    public List<MultipartFile> getImages() {
        return images;
    }
}
