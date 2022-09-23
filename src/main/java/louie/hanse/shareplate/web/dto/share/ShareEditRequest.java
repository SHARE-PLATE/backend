package louie.hanse.shareplate.web.dto.share;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Setter;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.validator.share.ValidShareImage;
import louie.hanse.shareplate.validator.share.ValidShareLatitude;
import louie.hanse.shareplate.validator.share.ValidShareLongitude;
import org.springframework.web.multipart.MultipartFile;

@Setter
public class ShareEditRequest {

    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private ShareType type;

    @NotEmpty(message = "요청한 쉐어정보 값이 비어있습니다.")
    @Size(max = 5, message = "이미지 5개를 초과하였습니다.")
    private List<@Valid @NotNull @ValidShareImage MultipartFile> images;

    @NotEmpty(message = "요청한 쉐어정보 값이 비어있습니다.")
    private String title;

    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    @Positive(message = "요청값은 양수여야 합니다.")
    private Integer price;

    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    @Positive(message = "요청값은 양수여야 합니다.")
    private Integer originalPrice;

    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    @Positive(message = "요청값은 양수여야 합니다.")
    private Integer recruitment;

    @NotEmpty(message = "요청한 쉐어정보 값이 비어있습니다.")
    private String location;

    @NotEmpty(message = "요청한 쉐어정보 값이 비어있습니다.")
    private String locationGuide;

    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private Boolean locationNegotiation;

    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private Boolean priceNegotiation;

    private List<@Valid @NotEmpty(message = "요청한 쉐어정보 값이 비어있습니다.") String> hashtags;

    @ValidShareLatitude
    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private Double latitude;

    @ValidShareLongitude
    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private Double longitude;

    @NotEmpty(message = "요청한 쉐어정보 값이 비어있습니다.")
    private String description;

    @Future(message = "약속 시간은 현재 시간 이후로 설정해야 합니다.")
    @NotNull(message = "요청한 쉐어정보 값이 비어있습니다.")
    private LocalDateTime closedDateTime;

    public Share toEntity(Long id, Member member) {
        return new Share(id, member, type, title, price, originalPrice, recruitment, location,
            latitude, longitude, description, closedDateTime, locationGuide, locationNegotiation,
            priceNegotiation);
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public List<String> getHashtags() {
        return hashtags;
    }
}
