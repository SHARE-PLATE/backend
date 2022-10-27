package louie.hanse.shareplate.integration.keyword;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.KeywordExceptionType.KEYWORD_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.KeywordExceptionType.KEYWORD_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.KeywordExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD;
import static louie.hanse.shareplate.exception.type.KeywordExceptionType.PATH_VARIABLE_EMPTY_KEYWORD_ID;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("키워드 삭제 통합 테스트")
class KeywordDeleteIntegrationTest extends InitIntegrationTest {

    @Test
    void 등록한_키워드를_삭제한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-delete"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void path_variable의_키워드_id가_null값일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-delete-failed-by-path-variable-empty-keyword-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(PATH_VARIABLE_EMPTY_KEYWORD_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_KEYWORD_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_KEYWORD_ID.getMessage()));
    }


    @Test
    void 키워드_id가_양수가_아닐_경우_예외를_발생시칸다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-delete-failed-by-keyword-id-not-positive"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -1)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(KEYWORD_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(KEYWORD_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(KEYWORD_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("keyword-delete-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 존재하지_않은_키워드_id일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-delete-failed-by-keyword-id-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 999)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(KEYWORD_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(KEYWORD_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(KEYWORD_NOT_FOUND.getMessage()));
    }

    @Test
    void 다른_회원의_키워드일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("keyword-delete-failed-by-other-member-delete-keyword"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 2)

            .when()
            .delete("/keywords/{id}")

            .then()
            .statusCode(OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD.getStatusCode().value())
            .body("errorCode", equalTo(OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD.getErrorCode()))
            .body("message", equalTo(OTHER_MEMBER_CAN_NOT_DELETE_KEYWORD.getMessage()));
    }
}
