package louie.hanse.shareplate.web.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.NotificationService;
import louie.hanse.shareplate.web.dto.notification.ActivityNotificationResponse;
import louie.hanse.shareplate.web.dto.notification.KeywordNotificationResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications/activity")
    public List<ActivityNotificationResponse> activityNotificationList(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return notificationService.getActivityNotificationList(memberId);
    }

    @GetMapping("/notifications/keyword")
    public List<KeywordNotificationResponse> keywordNotificationList(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        return notificationService.getKeywordNotificationList(memberId);
    }

    @DeleteMapping("/notifications/{id}")
    public void deleteOnlyOneNotification(
        @PathVariable(required = false) @NotNull(message = "PathVariable의 notificationId가 비어있습니다.") @Positive(message = "알림 id는 양수여야 합니다.") Long id,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        notificationService.delete(id, memberId);
    }

    @DeleteMapping("/notifications")
    public void deleteSelectionNotification(
        @RequestBody Map<String, @Valid List<@Valid @NotNull(message = "PathVariable의 notificationId가 비어있습니다.") @Positive(message = "알림 id는 양수여야 합니다.") Long>> map,
        HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");
        List<Long> idList = map.get("idList");
        notificationService.deleteAll(idList, memberId);
    }
}
