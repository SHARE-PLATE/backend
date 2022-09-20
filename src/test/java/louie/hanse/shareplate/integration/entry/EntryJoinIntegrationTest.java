package louie.hanse.shareplate.integration.entry;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import louie.hanse.shareplate.exception.type.EntryExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

@DisplayName("쉐어 참여 기능 통합테스트")
class EntryJoinIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ShareService shareService;

    @Test
    void 회원이_쉐어에_참가_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("entry-request-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 3)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());
    }


    @Test
    void 회원이_쉐어_id_값을_빈값으로_참가를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("entry-request-empty-of-share-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", " ")

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID.getMessage()));
    }

    @Test
    void 회원이_쉐어_id_값을_음수로_참가를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("entry-request-negative-of-share-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", -3)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_ID_IS_NEGATIVE.getMessage()));
    }


    @Test
    void 유효하지_않은_회원이_쉐어에_참가_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("entry-request-share-by-invalid-member"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 3)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_유효하지_않은_쉐어에_참가_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-request-invalid-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 2222)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(ShareExceptionType.SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_이미_참가한_쉐어에_참가_재요청_한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-re-request-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 1)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(EntryExceptionType.SHARE_ALREADY_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(EntryExceptionType.SHARE_ALREADY_JOINED.getErrorCode()))
            .body("message", equalTo(EntryExceptionType.SHARE_ALREADY_JOINED.getMessage()));
    }

    @Test
    void 회원이_모집_정원이_초과된_쉐어에_참가_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("entry-request-exceeded-recruitment"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", 1)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(EntryExceptionType.SHARE_OVERCAPACITY.getStatusCode().value())
            .body("errorCode", equalTo(EntryExceptionType.SHARE_OVERCAPACITY.getErrorCode()))
            .body("message", equalTo(EntryExceptionType.SHARE_OVERCAPACITY.getMessage()));
    }

    @Test
    void 회원이_마감시간이_지난_쉐어에_참가_요청한다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        ShareRegisterRequest request = getShareRegisterRequest(
            LocalDateTime.now().minusHours(2));
        Long shareId = shareService.register(request, 2355841033L);

        given(documentationSpec)
            .filter(document("entry-request-closed-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("shareId", shareId)

            .when()
            .post("/shares/{shareId}/entry")

            .then()
            .statusCode(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN.getStatusCode().value())
            .body("errorCode",
                equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN.getErrorCode()))
            .body("message",
                equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN.getMessage()));
    }

    private ShareRegisterRequest getShareRegisterRequest(LocalDateTime closedDateTime) {
        MockMultipartFile image = new MockMultipartFile(
            "이미지", "test.txt".getBytes(StandardCharsets.UTF_8));

        ShareRegisterRequest request = new ShareRegisterRequest(ShareType.DELIVERY,
            List.of(image),
            "테스트를 위한 제목", 3000, 12000, 3,
            "강남역", "코드스쿼드", true, false, List.of("피자", "도미노피자"),
            37.498095, 127.027610, "도미노 피자에 대해서 어쩌구",
            closedDateTime);
        return request;
    }

}
