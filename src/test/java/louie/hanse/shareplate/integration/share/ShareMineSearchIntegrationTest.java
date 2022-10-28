package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.EMPTY_SHARE_INFO;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.INCORRECT_MINE_VALUE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.INCORRECT_TYPE_VALUE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("내 쉐어 조회 통합 테스트")
class ShareMineSearchIntegrationTest extends InitIntegrationTest {

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
    void 쉐어_타입이_유효하지_않을_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("share-search-mine-get-failed-by-incorrect-type-value"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("mineType", "wish")
            .param("shareType", "ddddddd")
            .param("isExpired", "false")

            .when()
            .get("/shares/mine")

            .then()
            .statusCode(INCORRECT_TYPE_VALUE.getStatusCode().value())
            .body("errorCode", equalTo(INCORRECT_TYPE_VALUE.getErrorCode()))
            .body("message", equalTo(INCORRECT_TYPE_VALUE.getMessage()));
    }

    @Test
    void mineType이_유효하지_않을_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("share-search-mine-get-failed-by-incorrect-mine-value"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("mineType", "abcd")
            .param("shareType", "delivery")
            .param("isExpired", "false")

            .when()
            .get("/shares/mine")

            .then()
            .statusCode(INCORRECT_MINE_VALUE.getStatusCode().value())
            .body("errorCode", equalTo(INCORRECT_MINE_VALUE.getErrorCode()))
            .body("message", equalTo(INCORRECT_MINE_VALUE.getMessage()));
    }

    @Test
    void mineType이_null값인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("share-search-mine-get-failed-by-empty-share-info"))
            .accept(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .param("shareType", "delivery")
            .param("isExpired", "false")

            .when()
            .get("/shares/mine")

            .then()
            .statusCode(EMPTY_SHARE_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_SHARE_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_SHARE_INFO.getMessage()));
    }
}
