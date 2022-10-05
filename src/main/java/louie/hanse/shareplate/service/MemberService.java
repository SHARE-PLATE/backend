package louie.hanse.shareplate.service;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.repository.MemberRepository;
import louie.hanse.shareplate.uploader.FileUploader;
import louie.hanse.shareplate.web.dto.member.MemberChangeUserInfoRequest;
import louie.hanse.shareplate.web.dto.member.MemberUserInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileUploader fileUploader;

    @Transactional
    public void changeUserInfo(MemberChangeUserInfoRequest request, Long id) throws IOException {
        Member member = findByIdOrElseThrow(id);
        String profileImageUrl = fileUploader.uploadMemberImage(request.getProfileImage());
        member.changeProfileImageUrl(profileImageUrl);
        member.changeNickname(request.getNickname());
    }

    public MemberUserInfoResponse getUserInfo(Long id) {
        Member member = findByIdOrElseThrow(id);
        return new MemberUserInfoResponse(member);
    }

    public Member findByIdOrElseThrow(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new GlobalException(MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
