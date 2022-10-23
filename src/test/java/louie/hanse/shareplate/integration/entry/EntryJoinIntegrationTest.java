package louie.hanse.shareplate.integration.entry;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN;
import static louie.hanse.shareplate.exception.type.EntryExceptionType.SHARE_ALREADY_JOINED;
import static louie.hanse.shareplate.exception.type.EntryExceptionType.SHARE_OVERCAPACITY;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_IS_CANCELED;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.getShareRegisterRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.io.IOException;
import java.time.LocalDateTime;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 참여 기능 통합테스트")
class EntryJoinIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Test
    void 회원이_쉐어에_참가한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("entry-share-post"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 3)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("entryId", notNullValue());
    }


    @Test
    void 쉐어_id가_null값일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", " ")

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(PATH_VARIABLE_EMPTY_SHARE_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getMessage()));
    }

    @Test
    void 쉐어_id가_양수가_아닐_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", -3)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_ID_IS_NEGATIVE.getMessage()));
    }


    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 3)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 존재하지_않은_쉐어_id일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 2222)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 이미_참가한_쉐어일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 1)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_ALREADY_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ALREADY_JOINED.getErrorCode()))
            .body("message", equalTo(SHARE_ALREADY_JOINED.getMessage()));
    }

    @Test
    void 취소된_쉐어에_요청할_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 6)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_IS_CANCELED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_IS_CANCELED.getErrorCode()))
            .body("message", equalTo(SHARE_IS_CANCELED.getMessage()));
    }

    @Test
    void 모집_정원이_초과된_쉐어일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 1)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(SHARE_OVERCAPACITY.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_OVERCAPACITY.getErrorCode()))
            .body("message", equalTo(SHARE_OVERCAPACITY.getMessage()));
    }

    @Test
    void 마감시간이_지난_쉐어일_경우_예외를_발생시킨다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        ShareRegisterRequest request = getShareRegisterRequest(LocalDateTime.now().minusHours(2));
        Long shareId = shareService.register(request, 2355841033L).get("id");

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", shareId)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN.getStatusCode().value())
            .body("errorCode", equalTo(CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN.getErrorCode()))
            .body("message", equalTo(CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN.getMessage()));
    }
}
