package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_IS_CANCELED;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 상세 조회 통합 테스트")
class ShareDetailSearchIntegrationTest extends InitIntegrationTest {

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
            .body("writerId", equalTo(2370842997L))
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
    void 쉐어_id가_null인_경우_예외를_발생시킨다() {
        given(documentationSpec)
            .filter(document("share-detail-get"))
            .accept(APPLICATION_JSON_VALUE)
            .pathParam("id", "   ")

            .when()
            .get("/shares/{id}")

            .then()
            .statusCode(PATH_VARIABLE_EMPTY_SHARE_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getMessage()));
    }

    @Test
    void 쉐어_id가_양수가_아닌_경우_예외를_발생시킨다() {
        given(documentationSpec)
            .filter(document("share-detail-get"))
            .accept(APPLICATION_JSON_VALUE)
            .pathParam("id", -1)

            .when()
            .get("/shares/{id}")

            .then()
            .statusCode(SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 쉐어를_찾을_수_없는_경우_예외를_발생시킨다() {
        given(documentationSpec)
            .filter(document("share-detail-get"))
            .accept(APPLICATION_JSON_VALUE)
            .pathParam("id", 33333333333L)

            .when()
            .get("/shares/{id}")

            .then()
            .statusCode(SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 쉐어가_취소된_경우_예외를_발생시킨다() {
        given(documentationSpec)
            .filter(document("share-detail-get"))
            .accept(APPLICATION_JSON_VALUE)
            .pathParam("id", 6)

            .when()
            .get("/shares/{id}")

            .then()
            .statusCode(SHARE_IS_CANCELED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_IS_CANCELED.getErrorCode()))
            .body("message", equalTo(SHARE_IS_CANCELED.getMessage()));
    }
}
