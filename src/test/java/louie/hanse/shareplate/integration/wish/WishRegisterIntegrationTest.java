package louie.hanse.shareplate.integration.wish;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.exception.type.WishExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("위시 기능 통합 테스트")
public class WishRegisterIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 회원이_원하는_쉐어를_위시로_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 3);

        given(documentationSpec)
            .filter(document("wish-request-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .post("/wish-list")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_이미_위시_등록한_쉐어에_위시_등록을_재요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 1);

        given(documentationSpec)
            .filter(document("wish-re-request-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .post("/wish-list")

            .then()
            .statusCode(WishExceptionType.SHARE_ALREADY_WISH.getStatusCode().value())
            .body("errorCode", equalTo(WishExceptionType.SHARE_ALREADY_WISH.getErrorCode()))
            .body("message", equalTo(WishExceptionType.SHARE_ALREADY_WISH.getMessage()));
    }

    @Test
    void 회원이_유효하지_않은_쉐어에_위시_등록을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 333);

        given(documentationSpec)
            .filter(document("wish-request-invalid-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .post("/wish-list")

            .then()
            .statusCode(ShareExceptionType.SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_등록한_쉐어에_위시_등록을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        JSONObject requestParam = new JSONObject();
        requestParam.put("shareId", 2);

        given(documentationSpec)
            .filter(document("wish-request-by-writer"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParam)

            .when()
            .post("/wish-list")

            .then()
            .statusCode(WishExceptionType.WRITER_CAN_NOT_WISH.getStatusCode().value())
            .body("errorCode",
                equalTo(WishExceptionType.WRITER_CAN_NOT_WISH.getErrorCode()))
            .body("message", equalTo(WishExceptionType.WRITER_CAN_NOT_WISH.getMessage()));
    }

}
