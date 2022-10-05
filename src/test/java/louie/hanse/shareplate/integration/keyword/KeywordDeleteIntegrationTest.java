package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import louie.hanse.shareplate.exception.type.KeywordExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 삭제 통합 테스트")
class KeywordDeleteIntegrationTest extends InitIntegrationTest {

    @Test
    void 등록한_키워드_삭제를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given()
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_키워드_id_값을_빈값으로_삭제를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given()
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(
                KeywordExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getStatusCode().value())
            .body("errorCode",
                equalTo(KeywordExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getErrorCode()))
            .body("message",
                equalTo(KeywordExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getMessage()));
    }


    @Test
    void 회원이_키워드_id_값을_음수로_삭제_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given()
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -1)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(
                KeywordExceptionType.KEYWORD_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode",
                equalTo(KeywordExceptionType.KEYWORD_ID_IS_NEGATIVE.getErrorCode()))
            .body("message",
                equalTo(KeywordExceptionType.KEYWORD_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_키워드_삭제를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given()
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(
                MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode",
                equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message",
                equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_유효하지_않은_키워드를_삭제_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given()
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 999)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(
                KeywordExceptionType.KEYWORD_NOT_FOUND.getStatusCode().value())
            .body("errorCode",
                equalTo(KeywordExceptionType.KEYWORD_NOT_FOUND.getErrorCode()))
            .body("message",
                equalTo(KeywordExceptionType.KEYWORD_NOT_FOUND.getMessage()));
    }

    @Test
    void 다른_회원의_키워드를_삭제_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given()
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 2)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(
                KeywordExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD.getStatusCode().value())
            .body("errorCode",
                equalTo(KeywordExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD.getErrorCode()))
            .body("message",
                equalTo(KeywordExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD.getMessage()));
    }
}
