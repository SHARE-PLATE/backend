package louie.hanse.shareplate.integration.notification;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
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

@DisplayName("키워드 알림 조회 통합 테스트")
class KeywordNotificationSearchIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원이_키워드_알림_리스트_조회를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .filter(document("notification-keyword-list-get"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)

            .when()
            .get("/notifications/keyword")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", equalTo(3))
            .body("[0].shareLocation", equalTo("강남역"))
            .body("[0].shareId", equalTo(1))
            .body("[0].writerId", equalTo(2370842997L))
            .body("[0].shareTitle", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("[0].shareThumbnailImageUrl", equalTo(
                "https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B51.jpeg"))
            .body("[0].notificationCreatedDateTime", equalTo("2022-07-03 16:00"));
    }

    @Test
    void 유효하지_않은_회원인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("notification-keyword-list-get-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)

            .when()
            .get("/notifications/keyword")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

}
