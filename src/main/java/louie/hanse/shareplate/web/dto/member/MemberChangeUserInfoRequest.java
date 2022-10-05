package louie.hanse.shareplate.web.dto.member;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.validator.member.ValidMemberProfileImage;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MemberChangeUserInfoRequest {

    @ValidMemberProfileImage
    private MultipartFile profileImage;

    @NotBlank(message = "요청한 회원정보 값이 비어있습니다.")
    private String nickname;
}
