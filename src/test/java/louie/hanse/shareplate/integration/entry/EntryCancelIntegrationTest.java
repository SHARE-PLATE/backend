package louie.hanse.shareplate.integration.entry;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.getShareRegisterRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.io.IOException;
import java.time.LocalDateTime;
import louie.hanse.shareplate.exception.type.EntryExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 참여 취소 기능 통합테스트")
class EntryCancelIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;


    @Test
    void 회원이_쉐어_참가_취소_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("entry-request-cancel"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 2)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_쉐어_id_값을_빈값으로_참가_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("entry-request-cancel-empty-of-share-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", " ")

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID.getMessage()));
    }

    @Test
    void 회원이_쉐어_id_값을_음수로_참가_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("entry-request-cancel-negative-of-share-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", -2)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_쉐어_참가_취소_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("entry-request-cancel"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 2)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_유효하지_않은_쉐어에_참가_취소_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-request-invalid-cancel-share-by-invalid-member"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 2222)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(ShareExceptionType.SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_참가되어_있지_않은_쉐어에_참가_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-re-request-cancel-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 4)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(EntryExceptionType.SHARE_NOT_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(EntryExceptionType.SHARE_NOT_JOINED.getErrorCode()))
            .body("message", equalTo(EntryExceptionType.SHARE_NOT_JOINED.getMessage()));
    }

    @Test
    void 회원이_취소된_쉐어에_참가_취소를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);



        given(documentationSpec)
            .filter(document("entry-re-request-cancel-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 6)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(ShareExceptionType.SHARE_IS_CANCELED.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_IS_CANCELED.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_IS_CANCELED.getMessage()));
    }

    @Test
    void 회원이_한시간_미만_남은_쉐어에_참가_취소_요청한다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        ShareRegisterRequest request = getShareRegisterRequest(LocalDateTime.now().plusMinutes(30));

        Long shareId = shareService.register(request, 2355841047L);

        given(documentationSpec)
            .filter(document("entry-request-left-than-an-hour"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", shareId)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(EntryExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME.getStatusCode().value())
            .body("errorCode",
                equalTo(EntryExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME.getErrorCode()))
            .body("message",
                equalTo(EntryExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME.getMessage()));
    }

    @Test
    void 회원이_모집이_지난_쉐어에_참가_취소_요청한다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        ShareRegisterRequest request = getShareRegisterRequest(LocalDateTime.now().minusHours(3));
        Long shareId = shareService.register(request, 2355841047L);

        given(documentationSpec)
            .filter(document("entry-request-cancel-closed-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", shareId)

            .when()
            .delete("/shares/{shareId}/entry")

            .then()
            .statusCode(
                EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getStatusCode().value())
            .body("errorCode",
                equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getErrorCode()))
            .body("message",
                equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getMessage()));
    }
}
