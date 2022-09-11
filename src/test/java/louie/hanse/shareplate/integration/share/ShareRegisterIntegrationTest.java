package louie.hanse.shareplate.integration.share;

import louie.hanse.shareplate.config.S3MockConfig;
import louie.hanse.shareplate.integration.InitIntegrationTest;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.repository.MemberRepository;
import louie.hanse.shareplate.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(S3MockConfig.class)
@DisplayName("쉐어 등록 통합 테스트")
class ShareRegisterIntegrationTest extends InitIntegrationTest {

    @Autowired
    ShareService shareService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MemberRepository memberRepository;

//    @Test
//    void 음식_공유를_하기_위해_쉐어를_등록한다() {
//        Member member = memberRepository.findAll().get(0);
//        String accessToken = jwtProvider.createAccessToken(member.getId());
//
//        given(documentationSpec)
//            .filter(document("share-register-post"))
//            .header(AUTHORIZATION, accessToken)
//            .contentType(MULTIPART)
//            .multiPart("images", "test.txt", "abc".getBytes(), MediaType.TEXT_PLAIN_VALUE)
//            .multiPart("images", "test.txt", "def".getBytes(), MediaType.TEXT_PLAIN_VALUE)
//            .formParam("type", "delivery")
//            .formParam("title", "제목")
//            .formParam("price", 10000)
//            .formParam("originalPrice", 30000)
//            .formParam("recruitment", 3)
//            .formParam("locationNegotiation", true)
//            .formParam("priceNegotiation", true)
//            .formParam("hashtags", List.of("해시태그1", "해시태그2"))
//            .formParam("locationGuide", "강남역 파출소 앞")
//            .formParam("location", "강남역")
//            .formParam("latitude", 37.498095)
//            .formParam("longitude", 127.027610)
//            .formParam("closedDateTime", "2022-12-30 14:00")
//            .formParam("description", "설명")
//
//            .when()
//            .post("/shares")
//
//            .then()
//            .statusCode(HttpStatus.OK.value());
//    }

}
