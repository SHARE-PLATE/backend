package louie.hanse.shareplate.web.dto.member;

import lombok.Getter;
import lombok.Setter;
import louie.hanse.shareplate.validator.member.ValidMemberImage;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MemberChangeUserInfoRequest {

    @ValidMemberImage
    private MultipartFile image;

    private String nickname;
}
