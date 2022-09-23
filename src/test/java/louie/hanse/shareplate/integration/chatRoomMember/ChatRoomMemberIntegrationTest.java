package louie.hanse.shareplate.integration.chatRoomMember;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
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

    @Test
    void 참가한_모든_채팅방에_대한_회원_정보를_조회한다() {
        String accessToken = jwtProvider.createAccessToken(2370842997L);

        given(documentationSpec)
            .filter(document("chatRoomMember-list-get"))
            .header(AUTHORIZATION, accessToken)

            .when()
            .get("/chatroom-members")

            .then()
            .statusCode(HttpStatus.OK.value())
            .body("idList", hasSize(5));
    }
}
