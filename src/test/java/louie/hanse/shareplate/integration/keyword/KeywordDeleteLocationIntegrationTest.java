package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.Map;
import louie.hanse.shareplate.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 주소 삭제 통합 테스트")
class KeywordDeleteLocationIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 등록한_키워드_주소_삭제를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, String> requestBody = Collections.singletonMap("location", "방이를");

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("keyword-request-delete-location"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/keywords")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 유효하지_않은_회원이_키워드_삭제를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        Map<String, String> requestBody = Collections.singletonMap("location", "방이동");

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("keyword-request-delete-location"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/keywords")

            .then()
            .statusCode(
                MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode",
                equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message",
                equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 필수_필드값이_빈값으로_키워드_삭제_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, String> requestBody = Collections.singletonMap("location", null);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("keyword-request-delete-location"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/keywords")

            .then()
            .statusCode(
                KeywordExceptionType.EMPTY_KEYWORD_INFO.getStatusCode().value())
            .body("errorCode",
                equalTo(KeywordExceptionType.EMPTY_KEYWORD_INFO.getErrorCode()))
            .body("message",
                equalTo(KeywordExceptionType.EMPTY_KEYWORD_INFO.getMessage()));
    }

    @Test
    void 회원이_등록하지_않은_주소로_삭제_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, String> requestBody = Collections.singletonMap("location", "묵현리");

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("keyword-request-delete-location"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/keywords")

            .then()
            .statusCode(
                KeywordExceptionType.KEYWORD_NOT_FOUND.getStatusCode().value())
            .body("errorCode",
                equalTo(KeywordExceptionType.KEYWORD_NOT_FOUND.getErrorCode()))
            .body("message",
                equalTo(KeywordExceptionType.KEYWORD_NOT_FOUND.getMessage()));
    }
}
