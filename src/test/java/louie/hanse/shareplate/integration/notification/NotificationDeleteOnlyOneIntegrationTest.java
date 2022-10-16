package louie.hanse.shareplate.integration.notification;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.NotificationExceptionType.NOTIFICATION_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.NotificationExceptionType.NOTIFICATION_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.NotificationExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION;
import static louie.hanse.shareplate.exception.type.NotificationExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.repository.MemberRepository;
import louie.hanse.shareplate.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("알림 단건 삭제 통합 테스트")
class NotificationDeleteOnlyOneIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원이_알림을_단건_삭제한다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("notification-only-one-delete"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 5)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 알림_id가_null값인_경우_예외를_발생시킨다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getMessage()));
    }

    @Test
    void 알림_id_값이_양수가_아닌_경우_예외를_발생시킨다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -5)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(NOTIFICATION_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(NOTIFICATION_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(NOTIFICATION_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원인_경우_예외를_발생시킨다() {

        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 5)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 유효하지_않은_알림인_경우_예외를_발생시킨다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3444)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(NOTIFICATION_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(NOTIFICATION_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(NOTIFICATION_NOT_FOUND.getMessage()));
    }

    @Test
    void 다른_회원의_알림인_경우_예외를_발생시킨다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 2)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION.getStatusCode().value())
            .body("errorCode", equalTo(OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION.getErrorCode()))
            .body("message", equalTo(OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION.getMessage()));
    }

}

