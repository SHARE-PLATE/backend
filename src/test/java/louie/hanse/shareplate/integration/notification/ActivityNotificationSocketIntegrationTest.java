package louie.hanse.shareplate.integration.notification;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.integration.InitSocketIntegrationTest;
import louie.hanse.shareplate.service.MemberService;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.type.ActivityType;
import louie.hanse.shareplate.web.dto.notification.ActivityNotificationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayName("활동 알림 통합 테스트")
class ActivityNotificationSocketIntegrationTest extends InitSocketIntegrationTest {

    @Autowired
    MemberService memberService;

    @Autowired
    ShareService shareService;

    @Test
    void 쉐어에_새로운_참여자가_생긴다면_쉐어를_참여하고_있던_사람들에게_알림을_전송한다()
        throws ExecutionException, InterruptedException, TimeoutException {
        String destination = "/queue/notifications/entries/" + 6;
        String subscribeAccessToken = jwtProvider.createAccessToken(2370842997L);

        stompSession.subscribe(createStompHeaders(subscribeAccessToken, destination),
            getStompSessionHandlerAdapter(ActivityNotificationResponse.class));

        Long shareId = 2L;
        Long recruitmentMemberId = 2355841033L;

        String accessToken = jwtProvider.createAccessToken(recruitmentMemberId);
        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", shareId)

            .when()
            .post("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());

        ActivityNotificationResponse result = (ActivityNotificationResponse) completableFuture.get(
            3, TimeUnit.SECONDS);

        Member member = memberService.findByIdOrElseThrow(recruitmentMemberId);
        Share share = shareService.findWithShareImageByIdOrElseThrow(shareId);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getRecruitmentMemberNickname()).isEqualTo(member.getNickname());
        assertThat(result.getNotificationCreatedDateTime()).isBefore(LocalDateTime.now());
        assertThat(result.getShareTitle()).isEqualTo(share.getTitle());
        assertThat(result.getShareThumbnailImageUrl()).isEqualTo(
            share.getShareImages().get(0).getImageUrl());
        assertThat(result.getShareId()).isEqualTo(share.getId());
        assertThat(result.getWriterId()).isEqualTo(share.getWriter().getId());
        assertThat(result.getActivityType()).isEqualTo(ActivityType.ENTRY);
    }

    @Test
    void 쉐어_참여를_취소한다면_해당_쉐어를_참여하고있던_사람들에게_알림을_전송한다()
        throws ExecutionException, InterruptedException, TimeoutException {
        String destination = "/queue/notifications/entries/" + 1;
        String subscribeAccessToken = jwtProvider.createAccessToken(2370842997L);

        stompSession.subscribe(createStompHeaders(subscribeAccessToken, destination),
            getStompSessionHandlerAdapter(ActivityNotificationResponse.class));

        Long memberId = 2355841047L;
        Long shareId = 1L;

        String accessToken = jwtProvider.createAccessToken(memberId);
        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", shareId)

            .when()
            .delete("/shares/{id}/entry")

            .then()
            .statusCode(HttpStatus.OK.value());

        ActivityNotificationResponse result = (ActivityNotificationResponse)
            completableFuture.get(3, TimeUnit.SECONDS);

        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findWithShareImageByIdOrElseThrow(shareId);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getRecruitmentMemberNickname()).isEqualTo(member.getNickname());
        assertThat(result.getNotificationCreatedDateTime()).isBefore(LocalDateTime.now());
        assertThat(result.getShareTitle()).isEqualTo(share.getTitle());
        assertThat(result.getShareThumbnailImageUrl()).isEqualTo(
            share.getShareImages().get(0).getImageUrl());
        assertThat(result.getShareId()).isEqualTo(share.getId());
        assertThat(result.getWriterId()).isEqualTo(share.getWriter().getId());
        assertThat(result.getActivityType()).isEqualTo(ActivityType.ENTRY_CANCEL);
    }

    @Test
    void 쉐어를_취소한다면_해당_쉐어를_참여하고_있던_사람들에게_알림이_전송된다()
        throws ExecutionException, InterruptedException, TimeoutException {
        String destination = "/queue/notifications/entries/" + 2;
        String subscribeAccessToken = jwtProvider.createAccessToken(2355841047L);

        stompSession.subscribe(createStompHeaders(subscribeAccessToken, destination),
            getStompSessionHandlerAdapter(ActivityNotificationResponse.class));

        Long memberId = 2370842997L;
        Long shareId = 1L;

        String accessToken = jwtProvider.createAccessToken(memberId);
        given(documentationSpec)
            .header(AUTHORIZATION, accessToken)
            .pathParam("id", shareId)

            .when()
            .delete("/shares/{id}")

            .then()
            .statusCode(HttpStatus.OK.value());

        ActivityNotificationResponse result = (ActivityNotificationResponse)
            completableFuture.get(3, TimeUnit.SECONDS);

        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findWithShareImageByIdOrElseThrow(shareId);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getRecruitmentMemberNickname()).isEqualTo(member.getNickname());
        assertThat(result.getNotificationCreatedDateTime()).isBefore(LocalDateTime.now());
        assertThat(result.getShareTitle()).isEqualTo(share.getTitle());
        assertThat(result.getShareThumbnailImageUrl()).isEqualTo(
            share.getShareImages().get(0).getImageUrl());
        assertThat(result.getShareId()).isEqualTo(share.getId());
        assertThat(result.getWriterId()).isEqualTo(share.getWriter().getId());
        assertThat(result.getActivityType()).isEqualTo(ActivityType.SHARE_CANCEL);
    }
}
