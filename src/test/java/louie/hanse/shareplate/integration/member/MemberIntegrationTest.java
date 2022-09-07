package louie.hanse.shareplate.integration.member;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("회원 기능 통합 테스트")
class MemberIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 특정_회원의_정보를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("member-get-information"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)

            .when()
            .get("/members")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("profileImageUrl", equalTo(
                "http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg"))
            .body("nickname", equalTo("한승연"))
            .body("email", equalTo("x_x_x@hanmail.net"));
    }

    @Test
    void 특정_회원의_정보를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        JSONObject requestParams = new JSONObject();
        requestParams.put("profileImageUrl", "https:s3.com");
        requestParams.put("nickname", "칸칸칸칸");
        requestParams.put("email", "email_test.com");

        given(documentationSpec)
            .filter(document("member-changed-user-information"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParams)

            .when()
            .patch("/members")
            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
