package louie.hanse.shareplate.web.dto.member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MemberChangeUserInfoRequest {


    private MultipartFile profileImage;

    private String nickname;
}
