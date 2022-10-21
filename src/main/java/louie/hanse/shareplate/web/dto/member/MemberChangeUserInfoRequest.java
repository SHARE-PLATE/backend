package louie.hanse.shareplate.web.dto.member;

import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.validator.member.ValidMemberProfileImage;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MemberChangeUserInfoRequest {

    @ValidMemberProfileImage
    private MultipartFile profileImage;

    private String nickname;
}
