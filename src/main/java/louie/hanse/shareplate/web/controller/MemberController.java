package louie.hanse.shareplate.web.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.MemberService;
import louie.hanse.shareplate.web.dto.member.MemberChangeUserInfoRequest;
import louie.hanse.shareplate.web.dto.member.MemberUserInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public MemberUserInfoResponse getUserInfo(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return memberService.getUserInfo(memberId);
    }

    @PutMapping
    public void changeUserInfo(MemberChangeUserInfoRequest memberChangeUserInfoRequest,
        HttpServletRequest request) throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        memberService.changeUserInfo(memberChangeUserInfoRequest, memberId);
    }

}
