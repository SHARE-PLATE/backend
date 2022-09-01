package louie.hanse.shareplate.integration;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.DATE;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

@DisplayName("로그인 기능 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginIntegrationTest {

    @LocalServerPort
    int port;

    RequestSpecification documentationSpec;

    @Autowired
    JwtProvider jwtProvider;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        documentationSpec = new RequestSpecBuilder()
            .addFilter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        prettyPrint(),
                        removeHeaders(HOST, CONTENT_LENGTH))
                    .withResponseDefaults(
                        prettyPrint(),
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING,
                            "Keep-Alive",
                            HttpHeaders.VARY))
            )
            .build();
    }

    @Test
    void 로그인_회원을_로그아웃_한다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJSZWZyZXNoLVRva2VuIiwiYXVkIjoiMjM1NTg0MTAzMyIsImlzcyI6ImxvdWllMXNlIiwiZXhwIjo5OTk5OTk5OTk5OSwiaWF0IjoxNjYxMTMxMTgxLCJtZW1iZXJJZCI6MjM1NTg0MTAzM30.NmdUynRyknjHpQJ0zbvoCNzVjjF4JNSB7Uhnql8cZF0";

        given(documentationSpec)
            .filter(document("login-logout-member"))
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

        String accessToken = jwtProvider.createAccessToken(2370842997L);
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJSZWZyZXNoLVRva2VuIiwiYXVkIjoiMjM3MDg0Mjk5NyIsImlzcyI6ImxvdWllMXNlIiwiZXhwIjo5OTk5OTk5OTk5OSwiaWF0IjoxNjYxMTMxMTgxLCJtZW1iZXJJZCI6MjM3MDg0Mjk5N30.YocTTr79m94e3fkMK3HiGe5U96WtHT4pvognZpeIS8A";

        given(documentationSpec)
            .filter(document("login-verification"))
            .contentType(ContentType.JSON)
            .header("Access-Token", accessToken)
            .header("Refresh-Token", refreshToken)

            .when()
            .get("/login/verification")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

}
