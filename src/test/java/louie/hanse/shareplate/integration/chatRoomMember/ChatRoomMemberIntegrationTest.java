package louie.hanse.shareplate.integration.chatRoomMember;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("채팅방의 회원 기능 통합 테스트")
class ChatRoomMemberIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

//    @Test
//    void 참가한_채팅방을_나간다() {
//        String accessToken = jwtProvider.createAccessToken(2370842997L);
//
//        given(documentationSpec)
//            .contentType(ContentType.JSON)
//            .filter(document("chatRoomMember-chatRoom-exit-delete"))
//            .header(AUTHORIZATION, accessToken)
//            .body(Collections.singletonMap("chatRoomId", 1))
//
//            .when()
//            .delete("/chatroom-members")
//
//            .then()
//            .statusCode(HttpStatus.OK.value());
//    }
}
