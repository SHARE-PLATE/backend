package louie.hanse.shareplate.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_WEEK;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String createAccessToken(Long memberId) {
        return createToken("Access-Token", memberId, Date.from(Instant.now().plusMillis(ONE_HOUR)));
    }

    public String createRefreshToken(Long memberId) {
        return createToken("Refresh-Token", memberId, Date.from(Instant.now().plusMillis(ONE_WEEK)));
    }

    private String createToken(String subject, Long memberId, Date expiresAt) {
        return JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withSubject(subject)
                .withAudience(memberId.toString())
                .withExpiresAt(expiresAt)
                .withIssuedAt(Date.from(Instant.now()))

                .withClaim("memberId", memberId)

                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }

    public Long decodeMemberId(String token) {
        return JWT.decode(token)
                .getClaim("memberId")
                .asLong();
    }
}
