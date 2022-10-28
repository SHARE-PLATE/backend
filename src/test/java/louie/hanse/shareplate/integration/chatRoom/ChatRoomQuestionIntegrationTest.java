package louie.hanse.shareplate.integration.chatRoom;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.WRITER_CAN_NOT_QUESTION_CHAT;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.EMPTY_SHARE_INFO;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ShareExceptionType.SHARE_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("1:1 채팅방 개설 통합 테스트")
public class ChatRoomQuestionIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원이_일대일_채팅을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-question-chat-post"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 1))

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 이미_입장한_문의_채팅방일_경우_입장했던_채팅방_id를_보내준다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .filter(document("chatRoom-joined-question-chatroom-id-post"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 2))

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 쉐어_id가_null값일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatRoom-question-chat-post-failed-by-share-id-null"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", null))

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(EMPTY_SHARE_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_SHARE_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_SHARE_INFO.getMessage()));
    }

    @Test
    void 쉐어_id가_양수가_아닐_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatRoom-question-chat-post-failed-by-share-id-not-positive"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", -1))

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(SHARE_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(SHARE_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("chatRoom-question-chat-post-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 1))

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 존재하지_않은_쉐어_id일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatRoom-question-chat-post-failed-by-share-id-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 999))

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(SHARE_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(SHARE_NOT_FOUND.getMessage()));
    }

    @Test
    void 내가_등록한_쉐어일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatRoom-question-chat-post-failed-by-writer-own-question-chat"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("shareId", 1))

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(WRITER_CAN_NOT_QUESTION_CHAT.getStatusCode().value())
            .body("errorCode", equalTo(WRITER_CAN_NOT_QUESTION_CHAT.getErrorCode()))
            .body("message", equalTo(WRITER_CAN_NOT_QUESTION_CHAT.getMessage()));
    }
}
