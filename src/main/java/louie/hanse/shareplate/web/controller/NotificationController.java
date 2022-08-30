package louie.hanse.shareplate.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.service.NotificationService;
import louie.hanse.shareplate.web.dto.notification.ActivityNotificationResponse;
import louie.hanse.shareplate.web.dto.notification.KeywordNotificationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
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

}
