package louie.hanse.shareplate.integration.member;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.MULTIPART;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.MEMBER_NOT_FOUND;
import static louie.hanse.shareplate.exception.type.MemberExceptionType.NOT_SUPPORT_IMAGE_TYPE;
import static louie.hanse.shareplate.integration.share.utils.ShareIntegrationTestUtils.createMultiPartSpecification;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import louie.hanse.shareplate.integration.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("회원 편집 통합 테스트")
public class MemberEditIntegrationTest extends InitIntegrationTest {

    @Test
    void 회원의_정보를_변경한다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .filter(document("member-change-user-info-put"))
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .multiPart("image", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart(createMultiPartSpecification("nickname", "정현석"))

            .when()
            .patch("/members")

            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 유효하지_않은_회원인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(1L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .multiPart("image", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
            .multiPart(createMultiPartSpecification("nickname", "정현석"))

            .when()
            .patch("/members")

            .then()
            .statusCode(MEMBER_NOT_FOUND.getStatusCode().value())
            .body("errorCode", equalTo(MEMBER_NOT_FOUND.getErrorCode()))
            .body("message", equalTo(MEMBER_NOT_FOUND.getMessage()));
    }

    @Test
    void image가_유효하지_않은_이미지_확장자인_경우_예외를_발생시킨다() {
        String accessToken = jwtProvider.createAccessToken(2355841033L);

        given(documentationSpec)
            .contentType(MULTIPART)
            .header(AUTHORIZATION, accessToken)
            .multiPart("image", "수정된 test1.txt", "abcde".getBytes(), TEXT_PLAIN_VALUE)
            .multiPart(createMultiPartSpecification("nickname", "칸이"))

            .when()
            .patch("/members")

            .then()
            .statusCode(NOT_SUPPORT_IMAGE_TYPE.getStatusCode().value())
            .body("errorCode", equalTo(NOT_SUPPORT_IMAGE_TYPE.getErrorCode()))
            .body("message", equalTo(NOT_SUPPORT_IMAGE_TYPE.getMessage()));
    }

//    @Test
//    void 닉네임이_빈값인_경우_예외를_발생시킨다() {
//        String accessToken = jwtProvider.createAccessToken(2355841033L);
//
//        given(documentationSpec)
//            .contentType(MULTIPART)
//            .header(AUTHORIZATION, accessToken)
//            .multiPart("image", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
//            .multiPart(createMultiPartSpecification("nickname", ""))
//
//            .when()
//            .patch("/members")
//
//            .then()
//            .statusCode(EMPTY_MEMBER_INFO.getStatusCode().value())
//            .body("errorCode", equalTo(EMPTY_MEMBER_INFO.getErrorCode()))
//            .body("message", equalTo(EMPTY_MEMBER_INFO.getMessage()));

//    }
//    @Test
//    void 이미지가_null값인_경우_예외를_발생시킨다() {
//        String accessToken = jwtProvider.createAccessToken(2355841033L);
//
//        given(documentationSpec)
//            .contentType(MULTIPART)
//            .header(AUTHORIZATION, accessToken)
//            .multiPart(createMultiPartSpecification("nickname", "정현석"))
//
//            .when()
//            .patch("/members")
//
//            .then()
//            .statusCode(EMPTY_MEMBER_INFO.getStatusCode().value())
//            .body("errorCode", equalTo(EMPTY_MEMBER_INFO.getErrorCode()))
//            .body("message", equalTo(EMPTY_MEMBER_INFO.getMessage()));

//    }
//    @Test
//    void 이미지를_1개_초과로_요청하여_정보를_변경한다() {
//        String accessToken = jwtProvider.createAccessToken(2355841033L);
//
//        given(documentationSpec)
//            .filter(document("member-changed-user-information-by-invalid-member"))
//            .contentType(MULTIPART)
//            .header(AUTHORIZATION, accessToken)
//            .multiPart("image", "수정된 test1.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
//            .multiPart("image", "수정된 test2.jpg", "abcde".getBytes(), IMAGE_JPEG_VALUE)
//            .multiPart(createMultiPartSpecification("nickname", "정현석"))
//
//            .when()
//            .patch("/members")
//
//            .then()
//            .statusCode(MemberExceptionType.PROFILE_IMAGE_LIMIT_EXCEEDED.getStatusCode().value())
//            .body("errorCode", equalTo(MemberExceptionType.PROFILE_IMAGE_LIMIT_EXCEEDED.getErrorCode()))
//            .body("message", equalTo(MemberExceptionType.PROFILE_IMAGE_LIMIT_EXCEEDED.getMessage()));
//    }

}
