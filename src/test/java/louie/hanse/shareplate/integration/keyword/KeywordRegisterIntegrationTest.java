package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.restassured.http.ContentType;
import java.util.Map;
import louie.hanse.shareplate.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 등록 통합 테스트")
class KeywordRegisterIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 새로_생긴_쉐어에_대한_알림을_받을_수_있도록_키워드를_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, Object> requestBody = Map.of(
            "location", "청담동",
            "latitude", 37.524159,
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 필수_필드값이_빈값으로_키워드를_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(KeywordExceptionType.EMPTY_KEYWORD_INFO.getStatusCode().value())
            .body("errorCode", equalTo(KeywordExceptionType.EMPTY_KEYWORD_INFO.getErrorCode()))
            .body("message", equalTo(KeywordExceptionType.EMPTY_KEYWORD_INFO.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_키워드를_등록한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "latitude", 37.524159,
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 중복으로_키워드를_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "latitude", 37.524159,
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(KeywordExceptionType.DUPLICATE_KEYWORD.getStatusCode().value())
            .body("errorCode", equalTo(KeywordExceptionType.DUPLICATE_KEYWORD.getErrorCode()))
            .body("message", equalTo(KeywordExceptionType.DUPLICATE_KEYWORD.getMessage()));
    }

    @Test
    void 대한민국_위도_경도가_아닌_지역을_키워드로_등록한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, Object> requestBody = Map.of(
            "location", "목동",
            "latitude", 30.524159,
            "longitude", 126.872879,
            "contents", "떡볶이"
        );

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/keywords")

            .then()
            .statusCode(ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA.getStatusCode().value())
            .body("errorCode", equalTo(ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA.getErrorCode()))
            .body("message", equalTo(ShareExceptionType.OUT_OF_SCOPE_FOR_KOREA.getMessage()));
    }

}
