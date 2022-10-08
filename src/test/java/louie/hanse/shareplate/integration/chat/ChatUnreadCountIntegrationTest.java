package louie.hanse.shareplate.integration.chat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("채팅 안읽은 개수 조회 기능")
public class ChatUnreadCountIntegrationTest extends InitIntegrationTest {

    @Test
    void 읽지않은_채팅의_개수를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chat-unread-count"))
            .header(AUTHORIZATION, accessToken)

            .when()
            .get("/chats/unread")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("count", equalTo(5));
    }

    @Test
    void 유효하지_않은_회원이_읽지않은_채팅의_개수를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("chat-unread-count"))
            .header(AUTHORIZATION, accessToken)

            .when()
            .get("/chats/unread")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }
}
