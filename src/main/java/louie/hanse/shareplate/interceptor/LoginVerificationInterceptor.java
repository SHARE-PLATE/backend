package louie.hanse.shareplate.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.service.LoginService;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LoginVerificationInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        String accessToken = request.getHeader("Access-Token");
        String refreshToken = request.getHeader("Refresh-Token");

        if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken)) {
            throw new GlobalException(MemberExceptionType.NOT_LOGIN_MEMBER);
        }

        try {
            jwtProvider.verifyAccessToken(accessToken);
            jwtProvider.verifyRefreshToken(refreshToken);
        } catch (JWTVerificationException exception) {
            throw new GlobalException(MemberExceptionType.NOT_LOGIN_MEMBER);
        }

        Long accessTokenMemberId = jwtProvider.decodeMemberId(accessToken);
        Long refreshTokenMemberId = jwtProvider.decodeMemberId(refreshToken);

        if (!refreshTokenMemberId.equals(accessTokenMemberId)) {
            throw new GlobalException(MemberExceptionType.NOT_LOGIN_MEMBER);
        }

        String findRefreshToken = loginService.findRefreshTokenByMemberId(refreshTokenMemberId);
        if (findRefreshToken == null || !findRefreshToken.equals(refreshToken)) {
            throw new GlobalException(MemberExceptionType.NOT_LOGIN_MEMBER);
        }
        return true;
    }
}
