package louie.hanse.shareplate.integration.chatRoomMember;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("채팅방의 회원 기능 통합 테스트")
class ChatRoomMemberSearchIntegrationTest extends InitIntegrationTest {

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
            .body("", hasSize(5));
    }
}
