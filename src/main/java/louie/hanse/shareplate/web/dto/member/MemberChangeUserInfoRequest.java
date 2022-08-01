package louie.hanse.shareplate.web.dto.member;

import lombok.Getter;

@Getter
public class MemberChangeUserInfoRequest {
    private String profileImageUrl;
    private String nickname;
    private String email;
}
