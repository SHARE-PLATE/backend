package louie.hanse.shareplate.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.AuthExceptionType;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.service.LoginService;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LogoutInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String accessToken = request.getHeader("Access-Token");
        String refreshToken = request.getHeader("Refresh-Token");

        if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken)) {
            throw new GlobalException(AuthExceptionType.EMPTY_TOKEN);
        }

        Long accessTokenMemberId = jwtProvider.decodeMemberId(accessToken);
        Long refreshTokenMemberId = jwtProvider.decodeMemberId(refreshToken);

        if (!refreshTokenMemberId.equals(accessTokenMemberId)) {
            throw new GlobalException(AuthExceptionType.NOT_EQUAL_MEMBER_ID_IN_TOKEN);
        }

        String findRefreshToken = loginService.findRefreshTokenByMemberId(refreshTokenMemberId);
        if (findRefreshToken == null || !findRefreshToken.equals(refreshToken)) {
            throw new GlobalException(AuthExceptionType.INVALID_REFRESH_TOKEN);
        }

        try {
            jwtProvider.verifyAccessToken(accessToken);
            jwtProvider.verifyRefreshToken(refreshToken);
        } catch (TokenExpiredException e) {
        } catch (JWTVerificationException e) {
            throw new GlobalException(AuthExceptionType.TAMPERING_TOKEN);
        }

        request.setAttribute("refreshTokenMemberId", refreshTokenMemberId);
        return true;
    }
}
