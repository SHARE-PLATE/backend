package louie.hanse.shareplate.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.DATE;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import louie.hanse.shareplate.exception.type.EntryExceptionType;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

@DisplayName("참가 기능 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EntryIntegrationTest {

    @LocalServerPort
    int port;

    RequestSpecification documentationSpec;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ShareService shareService;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        documentationSpec = new RequestSpecBuilder()
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        prettyPrint(),
                        removeHeaders(HOST, CONTENT_LENGTH))
                    .withResponseDefaults(
                        prettyPrint(),
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING,
                            "Keep-Alive",
                            HttpHeaders.VARY))
            )
            .build();
    }

    @Test
    void 회원이_쉐어에_참가_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("entry-request-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)

            .when()
            .post("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_유효하지_않은_쉐어에_참가_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-request-invalid-share"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 2222)

            .when()
            .post("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
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
            .pathParam("id", 1)

            .when()
            .post("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
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
            .pathParam("id", 1)

            .when()
            .post("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
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
            .pathParam("id", shareId)

            .when()
            .post("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN.getErrorCode()))
            .body("message", equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_JOIN.getMessage()));
    }

    @Test
    void 회원이_쉐어_참가_취소_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("entry-request-cancel"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

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
            .statusCode(HttpStatus.BAD_REQUEST.value())
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
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(EntryExceptionType.SHARE_ALREADY_CANCELED.getErrorCode()))
            .body("message", equalTo(EntryExceptionType.SHARE_ALREADY_CANCELED.getMessage()));
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
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode",
                equalTo(EntryExceptionType.CLOSE_TO_THE_APPOINTMENT_TIME.getErrorCode()))
            .body("message",
                equalTo(EntryExceptionType.CLOSE_TO_THE_APPOINTMENT_TIME.getMessage()));
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
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode",
                equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getErrorCode()))
            .body("message", equalTo(EntryExceptionType.CLOSED_DATE_TIME_HAS_PASSED_NOT_CANCEL.getMessage()));
    }

    private ShareRegisterRequest getShareRegisterRequest(LocalDateTime closedDateTime) {
        MockMultipartFile image = new MockMultipartFile(
            "이미지", "test.txt".getBytes(StandardCharsets.UTF_8));

        ShareRegisterRequest request = new ShareRegisterRequest(ShareType.DELIVERY,
            List.of(image),
            "테스트를 위한 제목", 3000, 12000, 3,
            "강남역", "코드스쿼드", true, List.of("피자", "도미노피자"),
            37.498095, 127.027610, "도미노 피자에 대해서 어쩌구",
            closedDateTime);
        return request;
    }
}
