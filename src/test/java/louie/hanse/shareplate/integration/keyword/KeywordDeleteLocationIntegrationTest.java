package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.KeywordExceptionType.EMPTY_KEYWORD_INFO;
import static louie.hanse.shareplate.exception.type.KeywordExceptionType.KEYWORD_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.Map;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 주소 삭제 통합 테스트")
class KeywordDeleteLocationIntegrationTest extends InitIntegrationTest {

    @Test
    void 등록한_키워드_주소를_삭제한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-delete-location-delete"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("location", "방이동"))

            .when()
            .delete("/keywords")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("location", "방이동"))

            .when()
            .delete("/keywords")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 필수_필드값이_null값일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("location", null))

            .when()
            .delete("/keywords")

            .then()
            .statusCode(EMPTY_KEYWORD_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_KEYWORD_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_KEYWORD_INFO.getMessage()));
    }

    @Test
    void 등록하지_않은_주소일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("location", "묵현리"))

            .when()
            .delete("/keywords")

            .then()
            .statusCode(KEYWORD_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(KEYWORD_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(KEYWORD_NOT_FOUND.getMessage()));
    }
}
