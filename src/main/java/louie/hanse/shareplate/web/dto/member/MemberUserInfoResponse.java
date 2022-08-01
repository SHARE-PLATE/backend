package louie.hanse.shareplate.web.dto.member;

import lombok.Getter;
import louie.hanse.shareplate.domain.Member;

@Getter
public class MemberUserInfoResponse {
    private String profileImageUrl;
    private String nickname;
    private String email;

    public MemberUserInfoResponse(Member member) {
        this.profileImageUrl = member.getProfileImageUrl();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }
}
