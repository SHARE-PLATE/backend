package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.Map;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 삭제 통합 테스트")
class KeywordDeleteLocationIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 등록한_키워드_주소_삭제를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, String> requestBody = Collections.singletonMap("location", "송파구");

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
}
