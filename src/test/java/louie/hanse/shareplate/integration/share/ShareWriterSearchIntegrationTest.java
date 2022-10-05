package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 기능 통합 테스트")
class ShareWriterSearchIntegrationTest extends InitIntegrationTest {

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
            .body("shareCount", equalTo(3))
            .body("shares", hasSize(3))
            .body("shares[0].id", equalTo(1))
            .body("shares[0].thumbnailUrl", containsString("https://"))
            .body("shares[0].title", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("shares[0].location", equalTo("강남역"))
            .body("shares[0].price", equalTo(10000))
            .body("shares[0].createdDateTime", equalTo("2022-08-03 16:00"))
            .body("shares[0].closedDateTime", equalTo("2023-08-03 16:00"));
    }
}
