package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.oauth.OauthUserInfo;
import louie.hanse.shareplate.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(OauthUserInfo oauthUserInfo) {
        Member member = memberRepository.findById(oauthUserInfo.getId()).orElse(null);

        if (member == null) {
            member = oauthUserInfo.toMember();
            memberRepository.save(member);
        }
        return member;
    }
}
