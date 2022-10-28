package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.KeywordExceptionType.EMPTY_KEYWORD_INFO;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 주소 조회 통합 테스트")
public class KeywordLocationSearchIntegrationTest extends InitIntegrationTest {

    @Test
    void 내가_등록한_키워드의_주소를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-search-location-get"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .param("location", "방이동")

            .when()
            .get("/keywords/location")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("longitude", equalTo(127.100136F))
            .body("latitude", equalTo(37.51326F))
            .body("keywords[0].id", equalTo(6))
            .body("keywords[0].contents", equalTo("햄버거"));
    }

    @Test
    void 회원이_등록한_키워드에_등록하지_않았던_주소를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-new-location-get"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .param("location", "가정동")

            .when()
            .get("/keywords/location")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("longitude", equalTo(null))
            .body("latitude", equalTo(null))
            .body("keywords", hasSize(0));
    }

    @Test
    void RequestParam값이_null값일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-search-location-get-failed-by-empty-keyword-info"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .param("location", " ")

            .when()
            .get("/keywords/location")

            .then()
            .statusCode(EMPTY_KEYWORD_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_KEYWORD_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_KEYWORD_INFO.getMessage()));
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("keyword-search-location-get-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .param("location", "목동")

            .when()
            .get("/keywords/location")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }



}
