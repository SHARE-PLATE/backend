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
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class ReissueAccessTokenInterceptor implements HandlerInterceptor {

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
            throw new GlobalException(AuthExceptionType.EMPTY_TOKEN);
        }

        Long refreshTokenMemberId;

        try {
            jwtProvider.verifyAccessToken(accessToken);
            throw new GlobalException(AuthExceptionType.NOT_EXPIRED_ACCESS_TOKEN);
        } catch (TokenExpiredException e) {

            try {
                jwtProvider.verifyRefreshToken(refreshToken);
            } catch (TokenExpiredException exception) {
                throw new GlobalException(AuthExceptionType.EXPIRED_REFRESH_TOKEN);
            } catch (JWTVerificationException exception) {
                throw new GlobalException(AuthExceptionType.TAMPERING_REFRESH_TOKEN);
            }

            Long accessTokenMemberId = jwtProvider.decodeMemberId(accessToken);
            refreshTokenMemberId = jwtProvider.decodeMemberId(refreshToken);

            if (!refreshTokenMemberId.equals(accessTokenMemberId)) {
                throw new GlobalException(AuthExceptionType.NOT_EQUAL_MEMBER_ID_IN_TOKEN);
            }

            String findRefreshToken = loginService.findRefreshTokenByMemberId(refreshTokenMemberId);
            if (findRefreshToken == null || !findRefreshToken.equals(refreshToken)) {
                throw new GlobalException(AuthExceptionType.INVALID_REFRESH_TOKEN);
            }
        } catch (JWTVerificationException e) {
            throw new GlobalException(AuthExceptionType.TAMPERING_ACCESS_TOKEN);
        }
        request.setAttribute("refreshTokenMemberId", refreshTokenMemberId);
        return true;
    }
}
