package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
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

@DisplayName("키워드 조회 통합 테스트")
class KeywordSearchIntegrationTest extends InitIntegrationTest {

//    TODO : 순서를 보장하지 않고 조회한 데이터를 테스트하도록 변경해보기
    @Test
    void 내가_등록한_키워드를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-search-get"))
            .header(AUTHORIZATION, accessToken)
            .accept(ContentType.JSON)

            .when()
            .get("/keywords")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(2));
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .accept(ContentType.JSON)

            .when()
            .get("/keywords")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }
}
