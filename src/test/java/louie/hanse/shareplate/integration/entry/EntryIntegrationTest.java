package louie.hanse.shareplate.integration.entry;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("참가 기능 통합 테스트")
public class EntryIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 특정_회원이_특정_쉐어에_참가_한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("entry-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 4)

            .when()
            .post("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 특정_회원의_특정_쉐어_참가를_취소한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-cancel"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .delete("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
