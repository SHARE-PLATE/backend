package louie.hanse.shareplate.integration.chatLog;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.EMPTY_CHATROOM_INFO;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("채팅 읽은 시간 갱신 통합 테스트")
public class ChatLogUpdateDateTimeIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원이_채팅을_읽은시간_기준으로_채팅_읽은_시간을_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatLog-recent-update-date-time-put"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 1))

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 채팅방_id가_null값일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", null))

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(EMPTY_CHATROOM_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_CHATROOM_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_CHATROOM_INFO.getMessage()));
    }

    @Test
    void 채팅방_id가_양수가_아닐_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", -1))

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(CHATROOM_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(CHATROOM_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(CHATROOM_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 1))

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 존재하지_않은_채팅방_id일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 999))

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(CHAT_ROOM_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(CHAT_ROOM_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(CHAT_ROOM_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_참여하지_않은_채팅방일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 4))

            .when()
            .put("/chat-logs/update-read-time")

            .then()
            .statusCode(CHAT_ROOM_NOT_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(CHAT_ROOM_NOT_JOINED.getErrorCode()))
            .body("message", equalTo(CHAT_ROOM_NOT_JOINED.getMessage()));
    }
}
