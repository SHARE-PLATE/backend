package louie.hanse.shareplate.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.service.MemberService;
import louie.hanse.shareplate.web.dto.member.MemberChangeLocationRequest;
import louie.hanse.shareplate.web.dto.member.MemberChangeUserInfoRequest;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PatchMapping
    public void changeUserInfo(@RequestBody MemberChangeUserInfoRequest memberChangeUserInfoRequest, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        memberService.changeUserInfo(memberChangeUserInfoRequest, memberId);
    }

    @PatchMapping("/location")
    public void changeLocation(@RequestBody MemberChangeLocationRequest memberChangeLocationRequest, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        memberService.changeLocation(memberChangeLocationRequest, memberId);
    }
}
