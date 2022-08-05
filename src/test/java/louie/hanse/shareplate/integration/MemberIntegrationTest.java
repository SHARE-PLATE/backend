package louie.hanse.shareplate.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
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
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberIntegrationTest {

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
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING, "Keep-Alive"))
            )
            .build();
    }

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
    void 특정_회원의_주소를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2363364736L);

        JSONObject requestParams = new JSONObject();
        requestParams.put("location", "화도읍");
        requestParams.put("longitude", 127.3007637);
        requestParams.put("latitude", 37.6576769);

        given(documentationSpec)
            .filter(document("member-changed-location"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestParams)

            .when()
            .patch("/members/location")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 특정_회원의_정보를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2363364736L);

        JSONObject requestParams = new JSONObject();
        requestParams.put("profileImageUrl", "https:s3.com");
        requestParams.put("nickname", "louie1se");
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
