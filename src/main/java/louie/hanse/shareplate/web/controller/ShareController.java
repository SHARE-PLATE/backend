package louie.hanse.shareplate.web.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.NotificationService;
import louie.hanse.shareplate.service.ShareService;
import louie.hanse.shareplate.type.ActivityType;
import louie.hanse.shareplate.web.dto.share.ShareCommonResponse;
import louie.hanse.shareplate.web.dto.share.ShareDetailResponse;
import louie.hanse.shareplate.web.dto.share.ShareEditRequest;
import louie.hanse.shareplate.web.dto.share.ShareMineSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationRequest;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchResponse;
import louie.hanse.shareplate.web.dto.share.ShareWriterResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/shares")
@RestController
@Validated
public class ShareController {

    private final ShareService shareService;
    private final NotificationService notificationService;

    @PostMapping
    public void register(@Valid ShareRegisterRequest shareRegisterRequest, HttpServletRequest request)
        throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        Long shareId = shareService.register(shareRegisterRequest, memberId);
        notificationService.saveKeywordNotificationAndSend(shareId, memberId);
    }

    @GetMapping
    public List<ShareSearchResponse> searchAroundMember(
        @Valid ShareSearchRequest shareSearchRequest) {
        return shareService.searchAroundMember(shareSearchRequest);
    }

    @GetMapping("/mine")
    public List<ShareSearchResponse> searchMine(
        @Valid ShareMineSearchRequest shareMineSearchRequest, HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return shareService.searchMine(shareMineSearchRequest, memberId);
    }

    @GetMapping("/{id}")
    public ShareDetailResponse getDetail(@PathVariable(required = false)
    @NotNull(message = "PathVariable의 shareId가 비어있습니다.")
    @Positive(message = "쉐어 id는 양수여야 합니다.") Long id, HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return shareService.getDetail(id, accessToken);
    }

    @PutMapping("/{id}")
    public void edit(@PathVariable(required = false) @NotNull(message = "PathVariable의 shareId가 비어있습니다.")
    @Positive(message = "쉐어 id는 양수여야 합니다.") Long id, @Valid ShareEditRequest shareEditRequest,
        HttpServletRequest request) throws IOException {
        Long memberId = (Long) request.getAttribute("memberId");
        shareService.edit(shareEditRequest, id, memberId);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable @Positive(message = "쉐어 id는 양수여야 합니다.") Long id,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        shareService.cancel(id, memberId);
        notificationService.saveActivityNotificationAndSend(id, memberId, ActivityType.SHARE_CANCEL);
    }

    @GetMapping("/recommendation")
    public List<ShareCommonResponse> recommendationAroundMember(
        @Valid ShareRecommendationRequest request) {
        return shareService.recommendationAroundMember(request);
    }

    @GetMapping("/writer")
    public ShareWriterResponse getWriteByMember(Long writerId) {
        return shareService.getWriteByMember(writerId);
    }
}
