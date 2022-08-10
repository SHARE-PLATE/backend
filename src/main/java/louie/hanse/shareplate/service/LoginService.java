package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.oauth.OauthUserInfo;
import louie.hanse.shareplate.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public Member login(OauthUserInfo oauthUserInfo) {
        Member member = memberRepository.findById(oauthUserInfo.getId()).orElse(null);

        if (member == null) {
            member = oauthUserInfo.toMember();
            memberRepository.save(member);
        }
        return member;
    }

    @Transactional
    public void updateRefreshToken(String refreshToken, Long id) {
        Member member = memberService.findMember(id);
        member.changeRefreshToken(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(Long id) {
        Member member = memberService.findMember(id);
        member.deleteRefreshToken();
    }

    public String findRefreshTokenByMemberId(Long id) {
        return memberService.findMember(id).getRefreshToken();
    }
}
