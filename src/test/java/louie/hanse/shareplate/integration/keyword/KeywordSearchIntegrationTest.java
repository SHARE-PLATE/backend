package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.restassured.http.ContentType;
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

    @Test
    void 내가_등록한_쉐어를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .accept(ContentType.JSON)

            .when()
            .get("/keywords")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(2))
            .body("[0].keywords", hasSize(2))
            .body("[0].location", equalTo("목동"))
            .body("[0].keywords[0].id", equalTo(1))
            .body("[0].keywords[0].contents", equalTo("떡볶이"));
    }
}
