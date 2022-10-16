package louie.hanse.shareplate.web.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.AuthExceptionType;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.oauth.OAuthProperties;
import louie.hanse.shareplate.oauth.OauthUserInfo;
import louie.hanse.shareplate.service.LoginService;
import louie.hanse.shareplate.service.OAuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final LoginService loginService;
    private final OAuthService oAuthService;
    private final OAuthProperties oAuthProperties;
    private final JwtProvider jwtProvider;

    @GetMapping("/login/form")
    public void redirectLoginForm(HttpServletResponse response) throws IOException {
        response.sendRedirect(oAuthProperties.getLoginFormUrl());
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> paramMap,
        HttpServletResponse response) {

        String code = paramMap.get("code");
        String oauthAccessToken;

        try {
            oauthAccessToken = oAuthService.getAccessToken(code);
        } catch (HttpClientErrorException e) {
            throw new GlobalException(AuthExceptionType.INCORRECT_AUTHORIZATION_CODE);
        }

        OauthUserInfo userInfo = oAuthService.getUserInfo(oauthAccessToken);

        Member member = loginService.login(userInfo);

        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        loginService.updateRefreshToken(refreshToken, member.getId());

        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
        return Collections.singletonMap("thumbnailImageUrl", member.getThumbnailImageUrl());
    }

    @GetMapping("/reissue/access-token")
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Long refreshTokenMemberId = (Long) request.getAttribute("refreshTokenMemberId");
        String reissueAccessToken = jwtProvider.createAccessToken(refreshTokenMemberId);
            response.setHeader("Access-Token", reissueAccessToken);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        Long refreshTokenMemberId = (Long) request.getAttribute("refreshTokenMemberId");
        loginService.deleteRefreshToken(refreshTokenMemberId);
    }

    @GetMapping("/login/verification")
    public void loginVerification() {
    }
}
