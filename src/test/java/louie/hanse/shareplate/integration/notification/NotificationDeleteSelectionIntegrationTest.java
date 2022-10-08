package louie.hanse.shareplate.integration.notification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
class NotificationDeleteSelectionIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원이_알림을_선택_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, List<Long>> requestBody = Map.of("idList",
            new ArrayList<>(List.of(3L, 4L)));

        given(documentationSpec)
            .filter(document("notification-request-delete-selection"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/notifications")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_알림_id_값을_빈값으로_선택_삭제를_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);
        List<Long> idList = new ArrayList<>();
        idList.add(null);
        idList.add(1L);

        Map<String, List<Long>> requestBody = Map.of("idList", idList);

        given(documentationSpec)
            .filter(document("notification-request-delete-selection"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/notifications")

            .then()
            .statusCode(NotificationExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getStatusCode().value())
            .body("errorCode",
                equalTo(NotificationExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getErrorCode()))
            .body("message",
                equalTo(NotificationExceptionType.PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getMessage()));
    }

    @Test
    void 회원이_알림_id_값을_음수로_선택_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, List<Long>> requestBody = Map.of("idList",
            new ArrayList<>(List.of(3L, -4L)));

        given(documentationSpec)
            .filter(document("notification-request-delete-negative-selection"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/notifications")

            .then()
            .statusCode(
                NotificationExceptionType.NOTIFICATION_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode",
                equalTo(NotificationExceptionType.NOTIFICATION_ID_IS_NEGATIVE.getErrorCode()))
            .body("message",
                equalTo(NotificationExceptionType.NOTIFICATION_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_알림을_선택_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(1L);

        Map<String, List<Long>> requestBody = Map.of("idList",
            new ArrayList<>(List.of(1L, 3L)));

        given(documentationSpec)
            .filter(document("notification-request-delete-selection-invalid-member"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/notifications")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_유효하지_않은_알림을_선택_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, List<Long>> requestBody = Map.of("idList",
            new ArrayList<>(List.of(3444444L, 433333L)));

        given(documentationSpec)
            .filter(document("notification-request-delete-invalid-selection"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/notifications")

            .then()
            .statusCode(
                NotificationExceptionType.NOTIFICATION_NOT_FOUND.getStatusCode().value())
            .body("errorCode",
                equalTo(NotificationExceptionType.NOTIFICATION_NOT_FOUND.getErrorCode()))
            .body("message",
                equalTo(NotificationExceptionType.NOTIFICATION_NOT_FOUND.getMessage()));
    }

    @Test
    void 다른_회원의_알림을_선택_삭제_요청한다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);

        Map<String, List<Long>> requestBody = Map.of("idList",
            new ArrayList<>(List.of(3L, 4L)));

        given(documentationSpec)
            .filter(document("notification-request-delete-selection-by-other-member"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/notifications")

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

