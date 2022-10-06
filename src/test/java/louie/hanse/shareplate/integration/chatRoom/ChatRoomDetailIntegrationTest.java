package louie.hanse.shareplate.integration.chatRoom;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("채팅방 상세 조회 통합 테스트")
public class ChatRoomDetailIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원의_채팅방_메세지를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-detail-of-member"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("chatRoomMemberId", equalTo(1))
            .body("share.id", equalTo(1))
            .body("share.writerId", equalTo(2370842997L))
            .body("share.thumbnailImageUrl", equalTo("https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B51.jpeg"))
            .body("share.title  ", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("share.price", equalTo(10000))
            .body("share.originalPrice", equalTo(30000))
            .body("share.cancel", equalTo(false))
            .body("share.currentRecruitment", equalTo(3))
            .body("share.finalRecruitment", equalTo(3))
            .body("chats[0].contents", equalTo("내용1"))
            .body("chats[0].writer", equalTo("정현석"))
            .body("chats[0].writerThumbnailImageUrl", equalTo("http://k.kakaocdn.net/dn/wtMIN/btrII2nrJAv/KWEi4dNNGqeBYjzr0KZGK1/img_110x110.jpg"))
            .body("chats[0].writtenDateTime", equalTo("2022-07-03 16:00"))
            .body("chats[0].writtenByMe", equalTo(true));
    }

    @Test
    void 회원이_ChatRoom_id_값을_빈값으로_채팅방을_상세조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-detail-of-member"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(ChatRoomExceptionType.PATH_VARIABLE_EMPTY_CHATROOM_ID.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.PATH_VARIABLE_EMPTY_CHATROOM_ID.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.PATH_VARIABLE_EMPTY_CHATROOM_ID.getMessage()));
    }

    @Test
    void 회원이_ChatRoom_id_값을_음수로_채팅방을_상세조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-detail-of-member"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -1)

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원이_채팅방_메세지를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-detail-of-member"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 유효하지_않은_채팅방_id로_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-detail-of-member"))
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 999)

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND.getMessage()));
    }
}

