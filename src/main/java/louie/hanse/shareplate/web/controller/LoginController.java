package louie.hanse.shareplate.web.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.oauth.OAuthProperties;
import louie.hanse.shareplate.oauth.OauthUserInfo;
import louie.hanse.shareplate.service.LoginService;
import louie.hanse.shareplate.service.OAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, String> redirectLogin(@RequestBody Map<String, String> paramMap,
        HttpServletResponse response) {

        String code = paramMap.get("code");
        String oauthAccessToken = oAuthService.getAccessToken(code);

        OauthUserInfo userInfo = oAuthService.getUserInfo(oauthAccessToken);

        Member member = loginService.login(userInfo);

        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        loginService.updateRefreshToken(refreshToken, member.getId());

        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        return Collections.singletonMap("thumbnailImageUrl", member.getThumbnailImageUrl());
    }

    @PostMapping("/reissue/access-token")
    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("Access-Token");
        String refreshToken = request.getHeader("Refresh-Token");

        try {
            jwtProvider.verifyAccessToken(accessToken);
            response.setStatus(HttpStatus.FORBIDDEN.value());
        } catch (TokenExpiredException e) {
            jwtProvider.verifyRefreshToken(refreshToken);

            Long accessTokenMemberId = jwtProvider.decodeMemberId(accessToken);
            Long refreshTokenMemberId = jwtProvider.decodeMemberId(refreshToken);

            if (!refreshTokenMemberId.equals(accessTokenMemberId)){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            String findRefreshToken = loginService.findRefreshTokenById(refreshTokenMemberId);
            if (findRefreshToken.equals(refreshToken) || findRefreshToken == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            String reissueAccessToken = jwtProvider.createAccessToken(refreshTokenMemberId);
            response.setHeader("Access-Token", reissueAccessToken);
        }

    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("Access-Token");
        String refreshToken = request.getHeader("Refresh-Token");

        Long accessTokenMemberId = jwtProvider.decodeMemberId(accessToken);
        Long refreshTokenMemberId = jwtProvider.decodeMemberId(refreshToken);

        if (!refreshTokenMemberId.equals(accessTokenMemberId)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        String findRefreshToken = loginService.findRefreshTokenById(refreshTokenMemberId);
        if (!findRefreshToken.equals(refreshToken) || findRefreshToken == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        try {
            jwtProvider.verifyAccessToken(accessToken);
            jwtProvider.verifyRefreshToken(refreshToken);
        } catch (TokenExpiredException e) {
        } catch (JWTVerificationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        loginService.deleteRefreshToken(refreshTokenMemberId);
    }

}
