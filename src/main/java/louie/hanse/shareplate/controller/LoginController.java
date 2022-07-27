package louie.hanse.shareplate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.oauth.OAuthProperties;
import louie.hanse.shareplate.oauth.OauthUserInfo;
import louie.hanse.shareplate.service.LoginService;
import louie.hanse.shareplate.service.OAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final OAuthService oAuthService;
    private final OAuthProperties oAuthProperties;
    private final JwtProvider jwtProvider;

    @GetMapping("/login/form")
    public void loginForm(HttpServletResponse response) throws IOException {
        response.sendRedirect(oAuthProperties.getLoginFormUrl());
    }

    @PostMapping("/login")
    public Map<String, String> redirectLogin(@RequestBody Map<String, String> paramMap, HttpServletResponse response) {

        String code = paramMap.get("code");
        String oauthAccessToken = oAuthService.getAccessToken(code);

        OauthUserInfo userInfo = oAuthService.getUserInfo(oauthAccessToken);

        Member member = loginService.login(userInfo);

        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        return Collections.singletonMap("thumbnailImageUrl", member.getThumbnailImageUrl());
    }
}
