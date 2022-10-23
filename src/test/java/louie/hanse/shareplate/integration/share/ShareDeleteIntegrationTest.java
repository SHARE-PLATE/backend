package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_CANCEL;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.IS_NOT_WRITER;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.getShareRegisterRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import java.io.IOException;
import java.time.LocalDateTime;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 삭제 통합 테스트")
class ShareDeleteIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Test
    void 본인이_등록한_쉐어를_삭제한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("share-delete"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 4)

            .when()
            .delete("/shares/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 쉐어_id_값이_양수가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
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
    void 유효하지_않는_쉐어인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
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
    void 쉐어_작성자가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .delete("/shares/{id}")

            .then()
            .statusCode(IS_NOT_WRITER.getStatusCode().value())
            .body("errorCode", equalTo(IS_NOT_WRITER.getErrorCode()))
            .body("message", equalTo(IS_NOT_WRITER.getMessage()));
    }

    @Test
    void 마감_시간이_한시간_미만으로_남은_쉐어인_경우_예외를_발생시킨다() throws IOException {
        Long writerId = 2370842997L;

        Long shareId = shareService.register(
            getShareRegisterRequest(LocalDateTime.now().plusMinutes(30)), writerId).get("id");

        String accessToken = jwtProvider.createAccessToken(writerId);

        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", shareId)

            .when()
            .delete("/shares/{id}")

            .then()
            .statusCode(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_CANCEL.getStatusCode().value())
            .body("errorCode", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_CANCEL.getErrorCode()))
            .body("message", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_CANCEL.getMessage()));
    }
}
