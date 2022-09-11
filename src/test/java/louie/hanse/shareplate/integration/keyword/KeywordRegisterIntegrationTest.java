package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.restassured.http.ContentType;
import java.util.Map;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 등록 통합 테스트")
class KeywordRegisterIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 새로_생긴_쉐어에_대한_알림을_받을_수_있도록_키워드를_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "latitude", 37.524159,
            "longitude", 126.872879,
            "keyword", "떡볶이"
        );

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

}
