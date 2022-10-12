package louie.hanse.shareplate.integration.chatRoomMember;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.getShareRegisterRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import louie.hanse.shareplate.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.service.ChatRoomService;
import louie.hanse.shareplate.service.EntryService;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.type.ChatRoomType;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("채팅방의 나가기 기능 통합 테스트")
class ChatRoomMemberChatRoomExitIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    EntryService entryService;

    @Autowired
    ChatRoomService chatRoomService;

    @Test
    void 쉐어에_참가한_회원이_채팅방을_나간다() {
        String accessToken = jwtProvider.createAccessToken(2398606895L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-delete"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 5))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원이_한시간_미만_남은_쉐어의_문의_채팅방을_나간다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        ShareRegisterRequest request = getShareRegisterRequest(LocalDateTime.now().plusMinutes(30));
        Long shareId = shareService.register(request, 2355841047L);
        chatRoomService.createQuestionChatRoom(2370842997L, shareId);

        Long chatRoomId = chatRoomService.findIdByShareIdAndType(shareId, ChatRoomType.QUESTION);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-by-invalid-member"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", chatRoomId))

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
    void 회원이_유효하지_않은_채팅방_나가기를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-by-invalid-member"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 999))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_참가하지_않은_채팅방_나가기를_요청한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-by-invalid-member"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 5))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED.getMessage()));
    }

    @Test
    void 회원이_쉐어_마감시간이_한시간_미만으로_남은_쉐어의_참가_채팅방_나가기를_요청한다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        ShareRegisterRequest request = getShareRegisterRequest(LocalDateTime.now().plusMinutes(30));
        Long shareId = shareService.register(request, 2355841047L);
        entryService.entry(shareId, 2370842997L);

        Long chatRoomId = chatRoomService.findIdByShareIdAndType(shareId, ChatRoomType.ENTRY);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoomMember-chatRoom-exit-by-invalid-member"))
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", chatRoomId))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(ChatRoomExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME.getMessage()));
    }

    @Test
    void 취소된_쉐어에_글쓴이가_채팅방_나가기를_요청한다() {
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
    void 취소되지_않은_쉐어에_글쓴이가_채팅방_나가기를_요청한다() {
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
