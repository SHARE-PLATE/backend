package louie.hanse.shareplate.integration.wish;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("위시 기능 통합 테스트")
public class WishIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 특정_회원이_특정_쉐어를_위시로_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 1);

        given(documentationSpec)
            .filter(document("wish-list-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION,accessToken)
            .body(requestParam)

            .when()
            .post("/wish-list")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 특정_회원이_특정_쉐어의_위시를_취소한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 2);

        given(documentationSpec)
            .filter(document("wish-list-cancel-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION,accessToken)
            .body(requestParam)

            .when()
            .delete("/wish-list")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
