package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.repository.MemberRepository;
import louie.hanse.shareplate.web.dto.member.MemberChangeLocationRequest;
import louie.hanse.shareplate.web.dto.member.MemberChangeUserInfoRequest;
import louie.hanse.shareplate.web.dto.member.MemberUserInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void changeLocation(MemberChangeLocationRequest request, Long id) {
//        TODO 커스텀 Exception
        Member member = findMember(id);
        member.changeLocation(request.getLocation());
        member.changeLongitude(request.getLongitude());
        member.changeLatitude(request.getLatitude());
    }

    @Transactional
    public void changeUserInfo(MemberChangeUserInfoRequest request, Long id) {
        //        TODO 커스텀 Exception
        Member member = findMember(id);
        member.changeProfileImageUrl(request.getProfileImageUrl());
        member.changeNickname(request.getNickname());
    }

    public MemberUserInfoResponse getUserInfo(Long id) {
        //        TODO 커스텀 Exception
        Member member = findMember(id);
        return new MemberUserInfoResponse(member);
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}