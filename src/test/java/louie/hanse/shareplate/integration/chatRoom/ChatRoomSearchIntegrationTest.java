package louie.hanse.shareplate.integration.chatRoom;

import static io.restassured.RestAssured.given;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.EMPTY_CHATROOM_INFO;
import static louie.hanse.shareplate.exception.type.ChatRoomExceptionType.INCORRECT_TYPE_VALUE;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("채팅방 리스트 조회 통합 테스트")
class ChatRoomSearchIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원의_채팅방_목록을_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-list-of-member"))
            .header(AUTHORIZATION, accessToken)
            .param("type", "entry")

            .when()
            .get("/chatrooms")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("[0].id", equalTo(1))
            .body("[0].chatRoomMemberId", equalTo(1))
            .body("[0].shareThumbnailImageUrl", equalTo("https://share-plate-file-upload.s3.ap-northeast-2.amazonaws.com/test/%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B51.jpeg"))
            .body("[0].currentRecruitment", equalTo(3))
            .body("[0].cancel", equalTo(false))
            .body("[0].recentMessage", equalTo("내용4"))
            .body("[0].recentMessageDataTime", equalTo("2022-10-03 16:00"))
            .body("[0].recruitmentMemberNicknames[0]", equalTo("한승연"))
            .body("[0].recruitmentMemberImageUrls[0]", equalTo("http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg"))
            .body("[0].unreadCount", equalTo(1));
    }

    @Test
    void 유효하지_않은_회원이_채팅방_목록을_조회한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-list-of-member"))
            .header(AUTHORIZATION, accessToken)
            .param("type", "entry")

            .when()
            .get("/chatrooms")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 유효하지_않은_type으로_채팅방_목록을_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .filter(document("chatRoom-list-of-member"))
            .header(AUTHORIZATION, accessToken)
            .param("type", "aaaa")

            .when()
            .get("/chatrooms")

            .then()
            .statusCode(INCORRECT_TYPE_VALUE.getStatusCode().value())
            .body("errorCode", equalTo(INCORRECT_TYPE_VALUE.getErrorCode()))
            .body("message", equalTo(INCORRECT_TYPE_VALUE.getMessage()));
    }

    @Test
    void 비어있는_type으로_채팅방_목록을_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .contentType(ContentType.JSON)
            .filter(document("chatRoom-list-of-member"))
            .header(AUTHORIZATION, accessToken)
            .param("type", "")

            .when()
            .get("/chatrooms")

            .then()
            .statusCode(EMPTY_CHATROOM_INFO.getStatusCode().value())
            .body("errorCode", equalTo(EMPTY_CHATROOM_INFO.getErrorCode()))
            .body("message", equalTo(EMPTY_CHATROOM_INFO.getMessage()));
    }

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

//    @Test
//    void 회원이_문의용_일대일_채팅방을_생성한다() {
//        String accessToken = jwtProvider.createAccessToken(2398606895L);
//
//        given(documentationSpec)
//            .contentType(ContentType.JSON)
//            .filter(document("chatRoom-for-inquire-in-question"))
//            .header(AUTHORIZATION, accessToken)
//            .body(Collections.singletonMap("shareId", 2))
//
//            .when()
//            .post("/chatrooms")
//
//            .then()
//            .statusCode(HttpStatus.OK.value());
//    }

}
