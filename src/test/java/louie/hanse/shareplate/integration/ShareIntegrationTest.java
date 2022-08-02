package louie.hanse.shareplate.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
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
import io.restassured.specification.RequestSpecification;
import louie.hanse.shareplate.config.S3MockConfig;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.repository.MemberRepository;
import louie.hanse.shareplate.service.ShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

@Import(S3MockConfig.class)
@DisplayName("쉐어 기능 통합 테스트")
@ExtendWith({RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShareIntegrationTest {

    @LocalServerPort
    int port;

    RequestSpecification documentationSpec;

    @Autowired
    ShareService shareService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MemberRepository memberRepository;

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
    void 음식_공유를_하기_위해_쉐어를_등록한다() {
        Member member = memberRepository.findAll().get(0);
        String accessToken = jwtProvider.createAccessToken(member.getId());

        given(documentationSpec)
            .filter(document("share-register"))
            .multiPart("images", "test.txt", "abc".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .multiPart("images", "test.txt", "def".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .formParam("type", "delivery")
            .formParam("title", "제목")
            .formParam("price", 10000)
            .formParam("originalPrice", 20000)
            .formParam("location", "강남역")
            .formParam("latitude", 37.498095)
            .formParam("longitude", 127.027610)
            .formParam("appointmentDateTime", "2022-12-30/14:00")
            .formParam("description", "설명")
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)

            .when()
            .post("/shares")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
