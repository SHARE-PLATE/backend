package louie.hanse.shareplate.integration.share;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.EMPTY_SHARE_INFO;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.IMAGE_LIMIT_EXCEEDED;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.IS_NOT_WRITER;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.NOT_SUPPORT_IMAGE_TYPE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.PAST_CLOSED_DATE_TIME;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.PATH_VARIABLE_EMPTY_SHARE_ID;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_INFO_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_IS_CANCELED;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_IS_CLOSED;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.getShareRegisterRequest;
import static louie.hanse.shareplate.integration.share.utils.ShareIntegrationTestUtils.createMultiPartSpecification;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import java.io.IOException;
import java.time.LocalDateTime;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("쉐어 편집 통합 테스트")
class ShareEditIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

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

    @Test
    void 이미지를_입력하지_않은_경우_예외가_발생한다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
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
            .statusCode(EMPTY_SHARE_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_SHARE_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_SHARE_INFO.getMessage()));
    }

    @Test
    void 지원하지_않은_이미지_형식인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "수정된 test1.txt", "abcde".getBytes(), TEXT_PLAIN_VALUE)
            .multiPart("images", "수정된 test2.txt", "fhgij".getBytes(), TEXT_PLAIN_VALUE)
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
            .statusCode(NOT_SUPPORT_IMAGE_TYPE.getStatusCode().value())
            .body("errorCode", equalTo(NOT_SUPPORT_IMAGE_TYPE.getErrorCode()))
            .body("message", equalTo(NOT_SUPPORT_IMAGE_TYPE.getMessage()));
    }

    @Test
    void 이미지의_개수가_5개가_넘는다면_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "test1.jpg", "abc1".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.jpg", "abc2".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test3.jpg", "abc3".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test4.jpg", "abc4".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test5.jpg", "abc5".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test6.jpg", "abc6".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 37.524159)
            .formParam("longitude", 126.872879)
            .formParam("closedDateTime", "2022-12-30 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(IMAGE_LIMIT_EXCEEDED.getStatusCode().value())
            .body("errorCode", equalTo(IMAGE_LIMIT_EXCEEDED.getErrorCode()))
            .body("message", equalTo(IMAGE_LIMIT_EXCEEDED.getMessage()));
    }

    @Test
    void 대한민국의_위도_범위를_벗어난_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 39)
            .formParam("longitude", 126.872879)
            .formParam("closedDateTime", "2022-12-30 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }

    @Test
    void 대한민국의_경도_범위를_벗어난_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 37.524159)
            .formParam("longitude", 123)
            .formParam("closedDateTime", "2022-12-30 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }

    @Test
    void 마감_시간이_과거인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)
            .multiPart("images", "test1.jpg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test1.jpeg", "abc".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart("images", "test2.png", "def".getBytes(), IMAGE_PNG_VALUE)
            .multiPart(createMultiPartSpecification("title", "제목"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그1"))
            .multiPart(createMultiPartSpecification("hashtags", "해시태그2"))
            .multiPart(createMultiPartSpecification("locationGuide", "강남역 파출소 앞"))
            .multiPart(createMultiPartSpecification("location", "강남역"))
            .multiPart(createMultiPartSpecification("description", "설명"))
            .formParam("type", "delivery")
            .formParam("price", 10000)
            .formParam("originalPrice", 30000)
            .formParam("recruitment", 3)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", true)
            .formParam("latitude", 37.524159)
            .formParam("longitude", 126.872879)
            .formParam("closedDateTime", "2000-12-30 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(PAST_CLOSED_DATE_TIME.getStatusCode().value())
            .body("errorCode", equalTo(PAST_CLOSED_DATE_TIME.getErrorCode()))
            .body("message", equalTo(PAST_CLOSED_DATE_TIME.getMessage()));
    }

    @Test
    void 모집_인원이_양수가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
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
            .formParam("recruitment", 0)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2022-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_INFO_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_INFO_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_INFO_IS_NEGATIVE.getMessage()));
    }

    @Test
    void price가_양수가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
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
            .formParam("price", -13000)
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
            .statusCode(SHARE_INFO_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_INFO_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_INFO_IS_NEGATIVE.getMessage()));
    }

    @Test
    void originalPrice가_양수가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
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
            .formParam("originalPrice", -1000)
            .formParam("recruitment", 2)
            .formParam("locationNegotiation", true)
            .formParam("priceNegotiation", false)
            .formParam("latitude", 37.500326)
            .formParam("longitude", 127.036087)
            .formParam("closedDateTime", "2022-12-31 14:00")

            .when()
            .put("/shares/{id}")

            .then()
            .statusCode(SHARE_INFO_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_INFO_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_INFO_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 쉐어_id가_null값인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", "  ")
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
            .statusCode(PATH_VARIABLE_EMPTY_SHARE_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_SHARE_ID.getMessage()));
    }

    @Test
    void 쉐어_id가_양수가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -3)
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
            .statusCode(SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_쉐어인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 33333333333L)
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
            .statusCode(SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 쉐어_작성자가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
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
            .statusCode(IS_NOT_WRITER.getStatusCode().value())
            .body("errorCode", equalTo(IS_NOT_WRITER.getErrorCode()))
            .body("message", equalTo(IS_NOT_WRITER.getMessage()));
    }

    @Test
    void 쉐어가_마감된_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 5)
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
            .statusCode(SHARE_IS_CLOSED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_IS_CLOSED.getErrorCode()))
            .body("message", equalTo(SHARE_IS_CLOSED.getMessage()));
    }

    @Test
    void 쉐어가_취소된_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 6)
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
            .statusCode(SHARE_IS_CANCELED.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_IS_CANCELED.getErrorCode()))
            .body("message", equalTo(SHARE_IS_CANCELED.getMessage()));
    }

    @Test
    void 마감_시간이_한시간_미만으로_남은_쉐어인_경우_예외를_발생시킨다() throws IOException {
        Long writerId = 2370842997L;

        Long shareId = shareService.register(
            getShareRegisterRequest(LocalDateTime.now().plusMinutes(30)), writerId);

        String accessToken = jwtProvider.createAccessToken(writerId);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", shareId)
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
            .statusCode(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT.getStatusCode().value())
            .body("errorCode", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT.getErrorCode()))
            .body("message", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME_CANNOT_EDIT.getMessage()));
    }
}
