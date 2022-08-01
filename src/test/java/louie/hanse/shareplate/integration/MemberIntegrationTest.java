package louie.hanse.shareplate.integration;

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

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

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
                                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING))
                )
                .build();
    }

    @Test
    void 특정_회원의_주소를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2363364736L);

        JSONObject requestParams = new JSONObject();
        requestParams.put("location", "화도읍");
        requestParams.put("longitude", 127.3007637);
        requestParams.put("latitude", 37.6576769);

        given(documentationSpec)
                .filter(document("test"))
                .contentType(ContentType.JSON)
                .header(AUTHORIZATION, accessToken)
                .body(requestParams)

                .when()
                .patch("/members/location")

                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
