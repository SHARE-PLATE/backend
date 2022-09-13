package louie.hanse.shareplate.integration.chatRoomMember;

import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.http.ContentType;
import java.util.Collections;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("채팅방의 회원 기능 통합 테스트")
class ChatRoomMemberIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 참가한_채팅방을_나간다() {
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
}
