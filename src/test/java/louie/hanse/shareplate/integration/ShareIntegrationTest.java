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
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
import org.springframework.http.HttpHeaders;
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
                        removeHeaders(CONTENT_LENGTH, CONNECTION, DATE, TRANSFER_ENCODING, "Keep-Alive",
                            HttpHeaders.VARY))
            )
            .build();
    }

    @Test
    void 음식_공유를_하기_위해_쉐어를_등록한다() {
        Member member = memberRepository.findAll().get(0);
        String accessToken = jwtProvider.createAccessToken(member.getId());

        given(documentationSpec)
            .filter(document("share-register-post"))
            .header(AUTHORIZATION, accessToken)
            .contentType(MULTIPART)
            .multiPart("images", "test.txt", "abc".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .multiPart("images", "test.txt", "def".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .formParam("type", "delivery")
            .formParam("title", "제목")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("location", "강남역")
            .formParam("latitude", 37.498095)
            .formParam("longitude", 127.027610)
            .formParam("appointmentDateTime", "2022-12-30 14:00")
            .formParam("description", "설명")

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
            .accept(APPLICATION_JSON_VALUE)
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
            .body("[0].title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("[0].location", equalTo("강남역"))
            .body("[0].price", equalTo(10000))
            .body("[0].originalPrice", equalTo(30000))
            .body("[0].currentRecruitment", equalTo(2))
            .body("[0].finalRecruitment", equalTo(3))
            .body("[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("[0].appointmentDateTime", equalTo("2023-08-03 16:00"));
    }

    @Test
    void 내가_신청한_쉐어를_조회한다() {
        Long memberId = memberRepository.findById(2355841047L)
            .orElseThrow().getId();

        String accessToken = jwtProvider.createAccessToken(memberId);

        given(documentationSpec)
            .filter(document("share-search-mine-entry-get"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("mineType", "entry")
            .param("shareType", "delivery")

            .when()
            .get("/shares/mine")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(1))
            .body("[0].id", equalTo(1))
            .body("[0].thumbnailUrl", containsString("https://"))
            .body("[0].title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("[0].location", equalTo("강남역"))
            .body("[0].price", equalTo(10000))
            .body("[0].originalPrice", equalTo(30000))
            .body("[0].currentRecruitment", equalTo(2))
            .body("[0].finalRecruitment", equalTo(3))
            .body("[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("[0].appointmentDateTime", equalTo("2023-08-03 16:00"));
    }

    @Test
    void 내가_등록한_쉐어를_조회한다() {
        Long memberId = memberRepository.findById(2370842997L)
            .orElseThrow().getId();

        String accessToken = jwtProvider.createAccessToken(memberId);

        given(documentationSpec)
            .filter(document("share-search-mine-writer-get"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("mineType", "writer")
            .param("shareType", "delivery")

            .when()
            .get("/shares/mine")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(1))
            .body("[0].id", equalTo(1))
            .body("[0].thumbnailUrl", containsString("https://"))
            .body("[0].title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("[0].location", equalTo("강남역"))
            .body("[0].price", equalTo(10000))
            .body("[0].originalPrice", equalTo(30000))
            .body("[0].currentRecruitment", equalTo(2))
            .body("[0].finalRecruitment", equalTo(3))
            .body("[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("[0].appointmentDateTime", equalTo("2023-08-03 16:00"));
    }

    @Test
    void 내가_찜한_쉐어를_조회한다() {
        Long memberId = memberRepository.findById(2370842997L)
            .orElseThrow().getId();

        String accessToken = jwtProvider.createAccessToken(memberId);

        given(documentationSpec)
            .filter(document("share-search-mine-wish-get"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("mineType", "wish")
            .param("shareType", "delivery")

            .when()
            .get("/shares/mine")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(1))
            .body("[0].id", equalTo(2))
            .body("[0].thumbnailUrl", containsString("https://"))
            .body("[0].title", equalTo("판교역에서 치킨 먹을 사람 모집합니다."))
            .body("[0].location", equalTo("판교역"))
            .body("[0].price", equalTo(7000))
            .body("[0].originalPrice", equalTo(28000))
            .body("[0].currentRecruitment", equalTo(1))
            .body("[0].finalRecruitment", equalTo(4))
            .body("[0].createdDateTime", equalTo("2022-07-03 16:00"))
            .body("[0].appointmentDateTime", equalTo("2023-07-03 16:00"));
    }

    @Test
    void 특정_쉐어의_상세정보를_조회한다() {
        given(documentationSpec)
            .filter(document("share-detail-get"))
            .accept(APPLICATION_JSON_VALUE)
            .pathParam("id", 1)

            .when()
            .get("/shares/{id}")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(1))
            .body("imageUrls", hasSize(2))
            .body("imageUrls[0]", containsString("https://"))
            .body("imageUrls[1]", containsString("https://"))
            .body("writer", equalTo("정현석"))
            .body("writerThumbnailImageUrl", equalTo("http://k.kakaocdn.net/dn/wtMIN/btrII2nrJAv/KWEi4dNNGqeBYjzr0KZGK1/img_110x110.jpg"))
            .body("title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("location", equalTo("강남역"))
            .body("latitude", equalTo(36.657677f))
            .body("longitude", equalTo(128.300764f))
            .body("description", equalTo("떡볶이 쉐어 설명"))
            .body("price", equalTo(10000))
            .body("originalPrice", equalTo(30000))
            .body("currentRecruitment", equalTo(2))
            .body("finalRecruitment", equalTo(3))
            .body("recruitmentMemberThumbnailImageUrls", hasSize(2))
            .body("recruitmentMemberThumbnailImageUrls[0]", containsString("http://"))
            .body("recruitmentMemberThumbnailImageUrls[1]", containsString("http://"))
            .body("createdDateTime", equalTo("2022-08-03 16:00"))
            .body("appointmentDateTime", equalTo("2023-08-03 16:00"))
            .body("wish", equalTo(false))
            .body("entry", equalTo(false));
    }

    @Test
    void 사용자_본인이_등록한_쉐어를_편집한다() {
        String accessToken = jwtProvider.createAccessToken(2355841022L);

        given(documentationSpec)
            .filter(document("share-edit-put"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "수정된 test1.txt", "abcde".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .multiPart("images", "수정된 test2.txt", "fhgij".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .formParam("type", "ingredient")
            .formParam("title", "수정된 제목")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("location", "역삼역")
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("appointmentDateTime", "2022-12-31 14:00")
            .formParam("description", "수정된 설명")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
