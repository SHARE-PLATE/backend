package louie.hanse.shareplate.jwt;

import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_WEEK;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final String issuer;
    private final Algorithm algorithm;

    public JwtProvider(JwtProperties jwtProperties) {
        this.issuer = jwtProperties.getIssuer();
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey());
    }

    public String createAccessToken(Long memberId) {
        return createToken("Access-Token", memberId, Date.from(Instant.now().plusMillis(ONE_HOUR)));
    }

    public String createRefreshToken(Long memberId) {
        return createToken("Refresh-Token", memberId,
            Date.from(Instant.now().plusMillis(ONE_WEEK)));
    }

    private String createToken(String subject, Long memberId, Date expiresAt) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withAudience(memberId.toString())
            .withExpiresAt(expiresAt)
            .withIssuedAt(Date.from(Instant.now()))

            .withClaim("memberId", memberId)

            .sign(algorithm);
    }

    public Long decodeMemberId(String token) {
        return JWT.decode(token)
            .getClaim("memberId")
            .asLong();
    }

    public void verifyAccessToken(String accessToken) {
        verifyToken("Access-Token", accessToken);
    }

    public void verifyRefreshToken(String refreshToken) {
        verifyToken("Refresh-Token", refreshToken);
    }

    private void verifyToken(String subject, String token) {
        JWT.require(algorithm)
            .withSubject(issuer)
            .withSubject(subject)
            .build()
            .verify(token);
    }
}
