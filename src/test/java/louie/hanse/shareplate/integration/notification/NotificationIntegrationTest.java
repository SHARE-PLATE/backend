package louie.hanse.shareplate.integration.notification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.repository.MemberRepository;
import louie.hanse.shareplate.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("알림 기능 통합 테스트")
public class NotificationIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 특정_회원의_활동_알림_리스트를_조회한다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("notification-activity-list"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)

            .when()
            .get("/notifications/activity")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].recruitmentMemberNickname", equalTo("정현석"))
            .body("[0].notificationCreatedDateTime", equalTo("2022-07-03 16:00"))
            .body("[0].shareTitle", equalTo("판교역에서 치킨 먹을 사람 모집합니다."))
            .body("[0].shareThumbnailImageUrl", equalTo(
                "https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/louie1se/%E1%84%8E%E1%85%B5%E1%84%8F%E1%85%B5%E1%86%AB1.jpeg"))
            .body("[0].shareId", equalTo(2))
            .body("[0].activityType", equalTo("ENTRY"));
    }

    @Test
    void 특정_회원의_키워드_알림_리스트를_조회한다() {

        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("notification-keyword-list"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)

            .when()
            .get("/notifications/keyword")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].shareLocation", equalTo("강남역"))
            .body("[0].shareId", equalTo(1))
            .body("[0].shareTitle", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("[0].shareThumbnailImageUrl", equalTo(
                "https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B51.jpeg"))
            .body("[0].notificationCreatedDateTime", equalTo("2022-07-03 16:00"));
    }

    @Test
    void 특정_회원의_알림을_단건_삭제한다() {

        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("notification-delete-only-one"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 3)

            .when()
            .delete("/notifications/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 특정_회원의_알림을_선택_삭제한다() {

        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, List<Long>> requestBody = Map.of("idList",
            new ArrayList<>(List.of(3L, 4L)));

        given(documentationSpec)
            .filter(document("notification-delete-all"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .delete("/notifications")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
