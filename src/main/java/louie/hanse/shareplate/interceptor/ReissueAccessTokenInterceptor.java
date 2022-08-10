package louie.hanse.shareplate.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
public class ReissueAccessTokenInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        String accessToken = request.getHeader("Access-Token");
        String refreshToken = request.getHeader("Refresh-Token");

        if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }

        Long accessTokenMemberId = jwtProvider.decodeMemberId(accessToken);
        Long refreshTokenMemberId = jwtProvider.decodeMemberId(refreshToken);

        try {
            jwtProvider.verifyAccessToken(accessToken);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        } catch (TokenExpiredException e) {

            try {
                jwtProvider.verifyRefreshToken(refreshToken);
            }catch (TokenExpiredException exception) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            } catch (JWTVerificationException exception) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            if (!refreshTokenMemberId.equals(accessTokenMemberId)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            String findRefreshToken = loginService.findRefreshTokenByMemberId(refreshTokenMemberId);
            if (findRefreshToken == null || !findRefreshToken.equals(refreshToken)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        } catch (JWTVerificationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        request.setAttribute("refreshTokenMemberId", refreshTokenMemberId);
        return true;
    }
}
