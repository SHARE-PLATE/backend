package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 조회 통합 테스트")
class KeywordSearchIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

//    TODO : 순서를 보장하지 않고 조회한 데이터를 테스트하도록 변경해보기
    @Test
    void 내가_등록한_키워드를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .accept(ContentType.JSON)

            .when()
            .get("/keywords")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(2));
    }

    @Test
    void 유효하지_않은_회원이_키워드를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .accept(ContentType.JSON)

            .when()
            .get("/keywords")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }
}
