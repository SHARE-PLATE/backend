package louie.hanse.shareplate.integration.chatRoomMember;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import louie.hanse.shareplate.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("채팅방의 나가기 기능 통합 테스트")
class ChatRoomMemberChatRoomExitIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 쉐어에_참가한_회원이_채팅방을_나간다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-delete"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 3))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 유효하지_않은_회원이_채팅방_나가기를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-by-invalid-member"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 1))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 취소된_쉐어의_글쓴이가_채팅방을_나가기를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-writer-of-uncanceled-share"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 7))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 취소되지_않은_쉐어의_글쓴이가_채팅방을_나가기를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-writer-of-canceled-share"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 1))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(ChatRoomExceptionType.SHARE_WRITER_CANNOT_LEAVE.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.SHARE_WRITER_CANNOT_LEAVE.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.SHARE_WRITER_CANNOT_LEAVE.getMessage()));
    }
}
