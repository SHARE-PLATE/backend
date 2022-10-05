package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.IS_NOT_WRITER;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 삭제 통합 테스트")
class ShareDeleteIntegrationTest extends InitIntegrationTest {

    @Test
    void 본인이_등록한_쉐어를_삭제한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-not-writer-delete"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 4)

            .when()
            .delete("/shares/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 요청한_쉐어_id_값이_음수인_경우_요청이_실패한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-delete"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -1)

            .when()
            .delete("/shares/{id}")

            .then()
            .statusCode(SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 존재하지_않은_쉐어_삭제_요청을_보내는_경우_요청은_실패한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-delete"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1000000000)

            .when()
            .delete("/shares/{id}")

            .then()
            .statusCode(SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 작성자가_아닌_사용자가_쉐어_삭제_요청을_보내는_경우_요청은_실패한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-delete"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .delete("/shares/{id}")

            .then()
            .statusCode(IS_NOT_WRITER.getStatusCode().value())
            .body("errorCode", equalTo(IS_NOT_WRITER.getErrorCode()))
            .body("message", equalTo(IS_NOT_WRITER.getMessage()));
    }
}
