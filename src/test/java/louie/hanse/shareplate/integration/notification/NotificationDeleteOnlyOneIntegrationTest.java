package louie.hanse.shareplate.integration.notification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.exception.type.NotificationExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.repository.MemberRepository;
import louie.hanse.shareplate.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("알림 삭제 기능 통합 테스트")
class NotificationDeleteOnlyOneIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원이_알림을_단건_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("notification-request-delete-only-one"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 5)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_알림_id_값을_빈값으로_단건_삭제를_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("notification-request-delete-empty-only-one"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(NotificationExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getStatusCode().value())
            .body("errorCode",
                equalTo(NotificationExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getErrorCode()))
            .body("message",
                equalTo(NotificationExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getMessage()));
    }

    @Test
    void 회원이_알림_id_값을_음수로_단건_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("notification-request-delete-negative-of-only-one"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -5)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(
                NotificationExceptionType.NOTIFICATION_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode",
                equalTo(NotificationExceptionType.NOTIFICATION_ID_IS_NEGATIVE.getErrorCode()))
            .body("message",
                equalTo(NotificationExceptionType.NOTIFICATION_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_알림을_단건_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("notification-request-delete-only-one-member"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 5)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_유효하지_않은_알림을_단건_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("notification-request-delete-invalid-only-one"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3444)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(
                NotificationExceptionType.NOTIFICATION_NOT_FOUND.getStatusCode().value())
            .body("errorCode",
                equalTo(NotificationExceptionType.NOTIFICATION_NOT_FOUND.getErrorCode()))
            .body("message",
                equalTo(NotificationExceptionType.NOTIFICATION_NOT_FOUND.getMessage()));
    }

    @Test
    void 다른_회원의_알림을_단건_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("notification-request-delete-only-one-by-other-member"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 2)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(
                NotificationExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION
                    .getStatusCode().value())
            .body("errorCode",
                equalTo(
                    NotificationExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION.getErrorCode()))
            .body("message",
                equalTo(
                    NotificationExceptionType.OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION.getMessage()));
    }

}

