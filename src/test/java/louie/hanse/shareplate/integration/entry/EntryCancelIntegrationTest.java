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

@DisplayName("쉐어 참여 취소 기능 통합테스트")
public class EntryCancelIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ShareService shareService;


    @Test
    void 회원이_쉐어_참가_취소_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("entry-request-cancel"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 2)

            .when()
            .delete("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_유효하지_않은_쉐어에_참가_취소_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-request-invalid-cancel-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 2222)

            .when()
            .delete("/shares/{id}/entry")

            .then()
            .statusCode(ShareExceptionType.SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_참가_취소한_쉐어에_취소_재요청을_한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-re-request-cancel-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 4)

            .when()
            .delete("/shares/{id}/entry")

            .then()
            .statusCode(EntryExceptionType.SHARE_NOT_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(EntryExceptionType.SHARE_NOT_JOINED.getErrorCode()))
            .body("message", equalTo(EntryExceptionType.SHARE_NOT_JOINED.getMessage()));
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
            .pathParam("id", shareId)

            .when()
            .delete("/shares/{id}/entry")

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
            .pathParam("id", shareId)

            .when()
            .delete("/shares/{id}/entry")

            .then()
            .statusCode(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getStatusCode().value())
            .body("errorCode",
                equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getErrorCode()))
            .body("message",
                equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getMessage()));
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
