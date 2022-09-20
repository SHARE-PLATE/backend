package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 주소 조회 통합 테스트")
public class KeywordLocationSearchIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 내가_등록한_키워드의_주소를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-request-keyword-location"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .param("location", "방이동")

            .when()
            .get("/keywords/location")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", equalTo(6))
            .body("[0].contents", equalTo("햄버거"));
    }

}
