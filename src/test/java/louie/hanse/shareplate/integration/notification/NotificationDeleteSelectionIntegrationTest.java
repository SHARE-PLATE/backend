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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.repository.MemberRepository;
import louie.hanse.shareplate.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("알림 선택 삭제 통합 테스트")
class NotificationDeleteSelectionIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원이_알림을_선택_삭제한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("notification-selection-delete"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("idList", List.of(3L, 4L)))

            .when()
            .delete("/notifications")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 알림_id가_null값인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        List<Long> idList = new ArrayList<>();
        idList.add(null);
        idList.add(1L);

        given(documentationSpec)
            .filter(document("notification-selection-delete-failed-by-notification-id-null"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("idList", idList))

            .when()
            .delete("/notifications")

            .then()
            .statusCode(PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_NOTIFICATION_ID.getMessage()));
    }

    @Test
    void 알림_id_값이_양수가_아닌_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("notification-selection-delete-failed-by-notification-id-not-positive"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("idList", List.of(3L, -4L)))

            .when()
            .delete("/notifications")

            .then()
            .statusCode(NOTIFICATION_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(NOTIFICATION_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(NOTIFICATION_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("notification-selection-delete-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("idList", List.of(1L, 3L)))

            .when()
            .delete("/notifications")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 유효하지_않은_알림인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("notification-selection-delete-failed-by-notification-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("idList", List.of(3444444L, 433333L)))

            .when()
            .delete("/notifications")

            .then()
            .statusCode(NOTIFICATION_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(NOTIFICATION_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(NOTIFICATION_NOT_FOUND.getMessage()));
    }

    @Test
    void 다른_회원의_알림인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("notification-selection-delete-failed-by-other-member-can-not-delete-notification"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("idList", List.of(3L, 4L)))

            .when()
            .delete("/notifications")

            .then()
            .statusCode(OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION.getStatusCode().value())
            .body("errorCode", equalTo(OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION.getErrorCode()))
            .body("message", equalTo(OTHER_MEMBER_CAN_NOT_DELETE_NOTIFICATION.getMessage()));
    }
}

