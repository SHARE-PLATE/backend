package louie.hanse.shareplate.integration.chatRoom;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CHATROOM_ID_IS_NEGATIVE;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.PATH_VARIABLE_EMPTY_CHATROOM_ID;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
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
            .filter(document("chatRoom-detail-get"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("chatRoomMemberId", equalTo(1))
            .body("type", equalTo("ENTRY"))
            .body("share.id", equalTo(1))
            .body("share.writerId", equalTo(2370842997L))
            .body("share.thumbnailImageUrl", equalTo(
                "https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B51.jpeg"))
            .body("share.title  ", equalTo("강남역에서 떡볶이 먹을 사람 모집합니다."))
            .body("share.price", equalTo(10000))
            .body("share.originalPrice", equalTo(30000))
            .body("share.cancel", equalTo(false))
            .body("share.currentRecruitment", equalTo(3))
            .body("share.finalRecruitment", equalTo(3))
            .body("share.location", equalTo("강남역"))
            .body("share.closedDateTime", equalTo("2023-08-03 16:00"))
            .body("share.writer", equalTo("정현석"))
            .body("chats", hasSize(4));
    }

    @Test
    void path_variable이_null일_경우() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatRoom-detail-get-failed-by-path-variable-empty-chatroom-id"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", " ")

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(PATH_VARIABLE_EMPTY_CHATROOM_ID.getStatusCode().value())
            .body("errorCode", equalTo(PATH_VARIABLE_EMPTY_CHATROOM_ID.getErrorCode()))
            .body("message", equalTo(PATH_VARIABLE_EMPTY_CHATROOM_ID.getMessage()));
    }

    @Test
    void 채팅방_id가_양수가_아닐_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatRoom-detail-get-failed-by-chatroom-id-not-positive"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", -1)

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(CHATROOM_ID_IS_NEGATIVE.getStatusCode().value())
            .body("errorCode", equalTo(CHATROOM_ID_IS_NEGATIVE.getErrorCode()))
            .body("message", equalTo(CHATROOM_ID_IS_NEGATIVE.getMessage()));
    }

    @Test
    void 유효하지_않은_회원일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("chatRoom-detail-get-failed-by-member-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 1)

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 존재하지_않은_채팅방_id일_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatRoom-detail-get-failed-by-chatroom-id-not-found"))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", 999)

            .when()
            .get("/chatrooms/{id}")

            .then()
            .statusCode(CHAT_ROOM_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(CHAT_ROOM_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(CHAT_ROOM_NOT_FOUND.getMessage()));
    }
}

