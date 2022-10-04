package louie.hanse.shareplate.integration.member;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static louie.hanse.shareplate.integration.share.utils.ShareIntegrationTestUtils.createMultiPartSpecification;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.exception.type.MemberExceptionType;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("회원 편집 통합 테스트")
public class MemberEditIntegrationTest extends InitIntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void 회원의_정보를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("member-changed-user-information"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .multiPart("profileImage", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart(createMultiPartSpecification("nickname", "정현석"))

            .when()
            .put("/members")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 유효하지_않은_회원이_정보를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .filter(document("member-changed-user-information-by-invalid-member"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .multiPart("profileImage", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart(createMultiPartSpecification("nickname", "정현석"))

            .when()
            .put("/members")

            .then()
            .statusCode(MemberExceptionType.MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void 닉네임을_빈값으로_요청하여_정보를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("member-changed-user-information-by-invalid-member"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .multiPart("profileImage", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart(createMultiPartSpecification("nickname", ""))

            .when()
            .put("/members")

            .then()
            .statusCode(MemberExceptionType.EMPTY_MEMBER_INFO.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.EMPTY_MEMBER_INFO.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.EMPTY_MEMBER_INFO.getMessage()));
    }
//
//    @Test
//    void 프로필이미지를_빈값으로_요청하여_정보를_변경한다() {
//        String accessToken = jwtProvider.createAccessToken(2355841033L);
//
//        given(documentationSpec)
//            .filter(document("member-changed-user-information-by-invalid-member"))
//            .contentType(MULTIPART)
//            .header(AUTHORIZATION, accessToken)
//            .multiPart(createMultiPartSpecification("nickname", "정현석"))
//
//            .when()
//            .put("/members")
//
//            .then()
//            .statusCode(MemberExceptionType.EMPTY_MEMBER_INFO.getStatusCode().value())
//            .body("errorCode", equalTo(MemberExceptionType.EMPTY_MEMBER_INFO.getErrorCode()))
//            .body("message", equalTo(MemberExceptionType.EMPTY_MEMBER_INFO.getMessage()));
//    }

//    @Test
//    void 프로필이미지를_1개_초과로_요청하여_정보를_변경한다() {
//        String accessToken = jwtProvider.createAccessToken(2355841033L);
//
//        given(documentationSpec)
//            .filter(document("member-changed-user-information-by-invalid-member"))
//            .contentType(MULTIPART)
//            .header(AUTHORIZATION, accessToken)
//            .multiPart("profileImage", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
//            .multiPart("profileImage", "수정된 test2.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
//            .multiPart(createMultiPartSpecification("nickname", "정현석"))
//
//            .when()
//            .put("/members")
//
//            .then()
//            .statusCode(MemberExceptionType.PROFILE_IMAGE_LIMIT_EXCEEDED.getStatusCode().value())
//            .body("errorCode", equalTo(MemberExceptionType.PROFILE_IMAGE_LIMIT_EXCEEDED.getErrorCode()))
//            .body("message", equalTo(MemberExceptionType.PROFILE_IMAGE_LIMIT_EXCEEDED.getMessage()));
//    }

    @Test
    void 유효하지_않은_이미지_확장자의_프로필이미지로_회원의_정보를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("member-changed-user-information"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .multiPart("profileImage", "수정된 test1.txt", "abcde".getBytes(), TEXT_PLAIN_VALUE)
            .multiPart(createMultiPartSpecification("nickname", "칸이"))

            .when()
            .put("/members")

            .then()
            .statusCode(MemberExceptionType.NOT_SUPPORT_IMAGE_TYPE.getStatusCode().value())
            .body("errorCode", equalTo(MemberExceptionType.NOT_SUPPORT_IMAGE_TYPE.getErrorCode()))
            .body("message", equalTo(MemberExceptionType.NOT_SUPPORT_IMAGE_TYPE.getMessage()));
    }
}
