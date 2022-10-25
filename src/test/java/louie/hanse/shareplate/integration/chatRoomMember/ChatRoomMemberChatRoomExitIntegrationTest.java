package louie.hanse.shareplate.integration.chatRoomMember;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CLOSE_TO_THE_CLOSED_DATE_TIME;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.EMPTY_CHATROOM_INFO;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.SHARE_WRITER_CANNOT_LEAVE;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static louie.hanse.shareplate.integration.entry.utils.EntryIntegrationTestUtils.getShareRegisterRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.repository.ChatRoomRepository;
import louie.hanse.shareplate.service.ChatRoomService;
import louie.hanse.shareplate.service.EntryService;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.type.ChatRoomType;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("채팅방 나가기 통합 테스트")
class ChatRoomMemberChatRoomExitIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    EntryService entryService;

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

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
    void 한시간_미만_남은_쉐어의_문의_채팅방을_나간다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        ShareRegisterRequest request = getShareRegisterRequest(LocalDateTime.now().plusMinutes(30));
        Long shareId = shareService.register(request, 2355841047L).get("id");
        chatRoomService.createQuestionChatRoom(2370842997L, shareId);

        Long chatRoomId = chatRoomRepository.findByShareIdAndType(shareId, ChatRoomType.QUESTION)
            .get(0).getId();

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", chatRoomId))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 취소된_쉐어의_글쓴이가_채팅방을_나간다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 7))

            .when()
            .delete("/chatroom-members")

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
            .delete("/chatroom-members")

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
            .delete("/chatroom-members")

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
            .delete("/chatroom-members")

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
            .delete("/chatroom-members")

            .then()
            .statusCode(CHAT_ROOM_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(CHAT_ROOM_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(CHAT_ROOM_NOT_FOUND.getMessage()));
    }

    @Test
    void 회원이_참가하지_않은_채팅방일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 5))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(CHAT_ROOM_NOT_JOINED.getStatusCode().value())
            .body("errorCode", equalTo(CHAT_ROOM_NOT_JOINED.getErrorCode()))
            .body("message", equalTo(CHAT_ROOM_NOT_JOINED.getMessage()));
    }

    @Test
    void 쉐어_마감시간이_한시간_미만으로_남은_쉐어의_참가_채팅방일_경우_예외를_발생시킨다() throws IOException {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        ShareRegisterRequest request = getShareRegisterRequest(LocalDateTime.now().plusMinutes(30));
        Long shareId = shareService.register(request, 2355841047L).get("id");
        entryService.entry(shareId, 2370842997L);

        Long chatRoomId = chatRoomRepository.findByShareIdAndType(shareId, ChatRoomType.ENTRY)
            .get(0).getId();

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", chatRoomId))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(CLOSE_TO_THE_CLOSED_DATE_TIME.getStatusCode().value())
            .body("errorCode", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME.getErrorCode()))
            .body("message", equalTo(CLOSE_TO_THE_CLOSED_DATE_TIME.getMessage()));
    }

    @Test
    void 취소되지_않은_쉐어의_글쓴이가_요청할_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .body(Collections.singletonMap("chatRoomId", 1))

            .when()
            .delete("/chatroom-members")

            .then()
            .statusCode(SHARE_WRITER_CANNOT_LEAVE.getStatusCode().value())
            .body("errorCode", equalTo(SHARE_WRITER_CANNOT_LEAVE.getErrorCode()))
            .body("message", equalTo(SHARE_WRITER_CANNOT_LEAVE.getMessage()));
    }
}
