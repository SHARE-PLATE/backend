package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static louie.hanse.shareplate.integration.share.ShareIntegrationTestUtils.createMultiPartSpecification;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 편집 통합 테스트")
class ShareEditIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 본인이_등록한_쉐어를_편집한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("share-edit-put"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "수정된 test2.png", "fhgij".getBytes(), IMAGE_PNG_VALUE)
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
}
