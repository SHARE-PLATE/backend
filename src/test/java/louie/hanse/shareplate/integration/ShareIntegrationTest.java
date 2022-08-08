package louie.hanse.shareplate.integration;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
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
@DisplayNameGeneration(ReplaceUnderscores.class)
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
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING, "Keep-Alive"))
            )
            .build();
    }

    @Test
    void 음식_공유를_하기_위해_쉐어를_등록한다() {
        Member member = memberRepository.findAll().get(0);
        String accessToken = jwtProvider.createAccessToken(member.getId());

        given(documentationSpec)
            .filter(document("share-register-post"))
            .multiPart("images", "test.txt", "abc".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .multiPart("images", "test.txt", "def".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .formParam("type", "delivery")
            .formParam("title", "제목")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("recruitmentLimit", true)
            .formParam("location", "강남역")
            .formParam("latitude", 37.498095)
            .formParam("longitude", 127.027610)
            .formParam("appointmentDateTime", "2022-12-30 14:00")
            .formParam("description", "설명")
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)

            .when()
            .post("/shares")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원_주변에_존재하면서_검색한_키워드가_포함된_쉐어를_조회한다() {
        Long memberId = memberRepository.findById(2370842997L)
            .orElseThrow().getId();
        String accessToken = jwtProvider.createAccessToken(memberId);

        given(documentationSpec)
            .filter(document("share-search-get"))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("type", "delivery")
            .param("keyword", "떡볶이")

            .when()
            .get("/shares")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(1))
            .body("[0].id", equalTo(1))
            .body("[0].thumbnailUrl", containsString("https://"))
            .body("[0].title", equalTo("떡볶이 먹을 사람 모집합니다."))
            .body("[0].location", equalTo("강남역"))
            .body("[0].price", equalTo(10000))
            .body("[0].originalPrice", equalTo(30000))
            .body("[0].currentRecruitment", equalTo(1))
            .body("[0].finalRecruitment", equalTo(3))
            .body("[0].recruitmentLimit", equalTo(true))
            .body("[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("[0].appointmentDateTime", equalTo("2023-08-03 16:00"));
    }

}
