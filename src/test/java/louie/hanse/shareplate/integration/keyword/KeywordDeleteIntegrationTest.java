package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 삭제 통합 테스트")
class KeywordDeleteIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 등록한_키워드를_삭제한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given()
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
