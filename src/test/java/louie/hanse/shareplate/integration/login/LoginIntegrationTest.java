package louie.hanse.shareplate.integration.login;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("로그인 통합 테스트")
class LoginIntegrationTest extends InitIntegrationTest {

    @Test
    void 로그인_회원을_로그아웃_한다() {

        Long memberId = 2355841033L;
        String accessToken = jwtProvider.createAccessToken(memberId);
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJSZWZyZXNoLVRva2VuIiwiYXVkIjoiMjM1NTg0MTAzMyIsImlzcyI6ImxvdWllMXNlIiwiZXhwIjo5OTk5OTk5OTk5OSwiaWF0IjoxNjYxMTMxMTgxLCJtZW1iZXJJZCI6MjM1NTg0MTAzM30.NmdUynRyknjHpQJ0zbvoCNzVjjF4JNSB7Uhnql8cZF0";

        given(documentationSpec)
            .filter(document("oauth-logout-post"))
            .contentType(ContentType.JSON)
            .header("Access-Token", accessToken)
            .header("Refresh-Token", refreshToken)

            .when()
            .post("/logout")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원의_로그인_여부를_확인한다() {

        Long memberId = 2370842997L;
        String accessToken = jwtProvider.createAccessToken(memberId);
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJSZWZyZXNoLVRva2VuIiwiYXVkIjoiMjM3MDg0Mjk5NyIsImlzcyI6ImxvdWllMXNlIiwiZXhwIjo5OTk5OTk5OTk5OSwiaWF0IjoxNjYxMTMxMTgxLCJtZW1iZXJJZCI6MjM3MDg0Mjk5N30.YocTTr79m94e3fkMK3HiGe5U96WtHT4pvognZpeIS8A";

        given(documentationSpec)
            .filter(document("oauth-login-verification-get"))
            .contentType(ContentType.JSON)
            .header("Access-Token", accessToken)
            .header("Refresh-Token", refreshToken)

            .when()
            .get("/login/verification")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

}
