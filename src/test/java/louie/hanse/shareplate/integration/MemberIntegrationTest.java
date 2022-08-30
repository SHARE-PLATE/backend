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

@DisplayName("회원 기능 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
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
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING, "Keep-Alive",
                            HttpHeaders.VARY))
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
