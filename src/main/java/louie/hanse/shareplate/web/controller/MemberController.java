package louie.hanse.shareplate.web.controller;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.service.MemberService;
import louie.hanse.shareplate.web.dto.member.MemberChangeLocationRequest;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @PatchMapping
    public void changeLocation(@RequestBody MemberChangeLocationRequest memberChangeLocationRequest, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        memberService.changeLocation(memberChangeLocationRequest, memberId);
    }
}
