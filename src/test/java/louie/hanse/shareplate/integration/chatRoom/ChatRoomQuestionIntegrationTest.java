package louie.hanse.shareplate.integration.chatRoom;

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

@DisplayName("1:1 채팅방 개설 통합 테스트")
public class ChatRoomQuestionIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원이_일대일_채팅을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2355841047L);

        Map<String, Long> requestBody = Collections.singletonMap("shareId", 1L);
        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-question"))
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_shareId를_null로_하여_일대일_채팅을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Long> requestBody = Collections.singletonMap("shareId", null);
        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-question"))
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(ChatRoomExceptionType.EMPTY_CHATROOM_INFO.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.EMPTY_CHATROOM_INFO.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.EMPTY_CHATROOM_INFO.getMessage()));
    }

    @Test
    void 회원이_shareId를_음수로로_하여_일대일_채팅을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Long> requestBody = Collections.singletonMap("shareId", -1L);
        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-question"))
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_일대일_채팅을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        Map<String, Long> requestBody = Collections.singletonMap("shareId", 1L);
        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-question"))
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 내가_등록한_쉐어에_일대일_채팅을_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        Map<String, Long> requestBody = Collections.singletonMap("shareId", 1L);
        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-question"))
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

            .when()
            .post("/chatrooms")

            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
