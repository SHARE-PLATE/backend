package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static louie.hanse.shareplate.integration.share.ShareIntegrationTestUtils.createMultiPartSpecification;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.config.S3MockConfig;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Import(S3MockConfig.class)
@DisplayName("쉐어 기능 통합 테스트")
class ShareIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 검색한_키워드가_포함된_회원_주변의_쉐어를_조회한다() {
        given(documentationSpec)
            .filter(document("share-search-get"))
            .accept(APPLICATION_JSON_VALUE)
            .param("type", "delivery")
            .param("keyword", "떡볶이")
            .param("latitude", 36.6576769)
            .param("longitude", 128.3007637)

            .when()
            .get("/shares")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("", hasSize(1))
            .body("[0].id", equalTo(1))
            .body("[0].thumbnailUrl", containsString("https://"))
            .body("[0].title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("[0].location", equalTo("강남역"))
            .body("[0].latitude", equalTo(36.657677f))
            .body("[0].longitude", equalTo(128.300764f))
            .body("[0].price", equalTo(10000))
            .body("[0].originalPrice", equalTo(30000))
            .body("[0].currentRecruitment", equalTo(3))
            .body("[0].finalRecruitment", equalTo(3))
            .body("[0].writerId", equalTo(2370842997L))
            .body("[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("[0].closedDateTime", equalTo("2023-08-03 16:00"));
    }

    @Test
    void 검색한_키워드가_포함된_회원_주변의_쉐어를_추천한다() {
        given(documentationSpec)
            .filter(document("share-recommendation-get"))
            .accept(APPLICATION_JSON_VALUE)
            .param("latitude", 36.6576769)
            .param("long천itude", 128.3007637)

            .when()
            .get("/shares/recommendation")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 내가_신청한_쉐어를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("share-search-mine-entry-get"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("mineType", "entry")
            .param("shareType", "delivery")
            .param("isExpired", "false")

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
            .body("[0].currentRecruitment", equalTo(3))
            .body("[0].finalRecruitment", equalTo(3))
            .body("[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("[0].closedDateTime", equalTo("2023-08-03 16:00"));
    }

    @Test
    void 내가_등록한_쉐어를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("share-search-mine-writer-get"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("mineType", "writer")
            .param("shareType", "delivery")
            .param("isExpired", "false")

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
            .body("[0].currentRecruitment", equalTo(3))
            .body("[0].finalRecruitment", equalTo(3))
            .body("[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("[0].closedDateTime", equalTo("2023-08-03 16:00"));
    }

    @Test
    void 내가_찜한_쉐어를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("share-search-mine-wish-get"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("mineType", "wish")
            .param("shareType", "delivery")
            .param("isExpired", "false")

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
            .body("[0].currentRecruitment", equalTo(3))
            .body("[0].finalRecruitment", equalTo(4))
            .body("[0].createdDateTime", equalTo("2022-07-03 16:00"))
            .body("[0].closedDateTime", equalTo("2023-07-03 16:00"));
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
            .body("writerThumbnailImageUrl", equalTo(
                "http://k.kakaocdn.net/dn/wtMIN/btrII2nrJAv/KWEi4dNNGqeBYjzr0KZGK1/img_110x110.jpg"))
            .body("title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("location", equalTo("강남역"))
            .body("latitude", equalTo(36.657677f))
            .body("longitude", equalTo(128.300764f))
            .body("description", equalTo("떡볶이 쉐어 설명"))
            .body("price", equalTo(10000))
            .body("originalPrice", equalTo(30000))
            .body("currentRecruitment", equalTo(3))
            .body("finalRecruitment", equalTo(3))
            .body("recruitmentMemberThumbnailImageUrls", hasSize(3))
            .body("recruitmentMemberThumbnailImageUrls[0]", containsString("http://"))
            .body("recruitmentMemberThumbnailImageUrls[1]", containsString("http://"))
            .body("createdDateTime", equalTo("2022-08-03 16:00"))
            .body("closedDateTime", equalTo("2023-08-03 16:00"))
            .body("wish", equalTo(false))
            .body("entry", equalTo(false))
            .body("wishCount", equalTo(1))
            .body("locationNegotiation", equalTo(true))
            .body("priceNegotiation", equalTo(false))
            .body("locationGuide", equalTo("강남역 1번 출구"))
            .body("hashtags", hasSize(2))
            .body("hashtags[0]", equalTo("해시태그 내용 1"));
    }

    @Test
    void 본인이_등록한_쉐어를_편집한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "수정된 test1.txt", "abcde".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .multiPart("images", "수정된 test2.txt", "fhgij".getBytes(), MediaType.TEXT_PLAIN_VALUE)
            .multiPart(createMultiPartSpecification("title", "수정된 제목"))
            .multiPart(createMultiPartSpecification("hashtags", "수정된 해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "수정된 해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "역삼역"))
            .multiPart(createMultiPartSpecification("description", "수정된 설명"))
            .formParam("type", "ingredient")
            .formParam("price", 13000)
            .formParam("originalPrice", 26000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2022-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 특정_사용자가_작성한_쉐어를_조회한다() {
        given(documentationSpec)
            .param("writerId", 2370842997L)
            .filter(document("share-write-by-member-get"))

            .when()
            .get("/shares/writer")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("writer", equalTo("정현석"))
            .body("thumbnailUrl", containsString("http://"))
            .body("shareCount", equalTo(1))
            .body("shares", hasSize(1))
            .body("shares[0].id", equalTo(1))
            .body("shares[0].thumbnailUrl", containsString("https://"))
            .body("shares[0].title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("shares[0].location", equalTo("강남역"))
            .body("shares[0].price", equalTo(10000))
            .body("shares[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("shares[0].closedDateTime", equalTo("2023-08-03 16:00"));
    }
}
