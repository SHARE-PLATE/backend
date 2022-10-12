package louie.hanse.shareplate.integration.chatLog;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import java.util.Map;
import louie.hanse.shareplate.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("채팅 읽은 시간 갱신 통합테스트")
public class ChatLogUpdateDateTimeIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원이_채팅을_읽은시간_기준으로_채팅_읽은_시간을_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Long> requestBody = Collections.singletonMap("chatRoomId", 1L);
        given(documentationSpec)
            .filter(document("chat-log-recent-update-date-time"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_chatRoomId를_빈값으로_하여_채팅_읽은시간_변경을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Long> requestBody = Collections.singletonMap("chatRoomId", null);
        given(documentationSpec)
            .filter(document("chat-log-recent-update-date-time"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(ChatRoomExceptionType.EMPTY_CHATROOM_INFO.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.EMPTY_CHATROOM_INFO.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.EMPTY_CHATROOM_INFO.getMessage()));
    }

    @Test
    void 회원이_chatRoomId를_음수값으로_하여_채팅_읽은시간_변경을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Long> requestBody = Collections.singletonMap("chatRoomId", -1L);
        given(documentationSpec)
            .filter(document("chat-log-recent-update-date-time"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_채팅_읽은시간_변경을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        Map<String, Long> requestBody = Collections.singletonMap("chatRoomId", 1L);
        given(documentationSpec)
            .filter(document("chat-log-recent-update-date-time"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_유효하지_않은_chatRoomId로_채팅_읽은시간_변경을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Long> requestBody = Collections.singletonMap("chatRoomId", 999L);
        given(documentationSpec)
            .filter(document("chat-log-recent-update-date-time"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_참여하지_않은_채팅방의_채팅_읽은시간_변경을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Long> requestBody = Collections.singletonMap("chatRoomId", 4L);
        given(documentationSpec)
            .filter(document("chat-log-recent-update-date-time"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED.getMessage()));
    }
}
