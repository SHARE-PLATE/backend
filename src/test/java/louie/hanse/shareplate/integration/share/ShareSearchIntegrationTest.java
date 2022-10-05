package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 조회 통합 테스트")
class ShareSearchIntegrationTest extends InitIntegrationTest {

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
    void 대한민국의_위도_범위를_벗어났을_경우_예외가_발생한다() {
        given(documentationSpec)
            .filter(document("share-search-get"))
            .accept(APPLICATION_JSON_VALUE)
            .param("type", "ingredient")
            .param("keyword", "햄버거")
            .param("latitude", 39)
            .param("longitude", 128.3007637)

            .when()
            .get("/shares")

            .then()
            .statusCode(OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }

    @Test
    void 대한민국의_경도_범위를_벗어났을_경우_예외가_발생한다() {
        given(documentationSpec)
            .filter(document("share-search-get"))
            .accept(APPLICATION_JSON_VALUE)
            .param("type", "delivery")
            .param("keyword", "햄버거")
            .param("latitude", 37.524159)
            .param("longitude", 123)

            .when()
            .get("/shares")

            .then()
            .statusCode(OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }
}
