package louie.hanse.shareplate.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ActivityNotification;
import louie.hanse.shareplate.domain.Notification;
import louie.hanse.shareplate.repository.NotificationRepository;
import louie.hanse.shareplate.web.dto.notification.KeywordNotificationResponse;
import louie.hanse.shareplate.web.dto.notification.ActivityNotificationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<ActivityNotificationResponse> getActivityNotificationList(Long memberId) {
        List<ActivityNotification> activityNotifications = notificationRepository
            .findAllActivityNotificationByMemberId(memberId);

        return activityNotifications.stream()
            .map(ActivityNotificationResponse::new)
            .collect(Collectors.toList());
    }

    public List<KeywordNotificationResponse> getKeywordNotificationList(Long memberId) {
        List<Notification> keywordNotifications = notificationRepository
            .findAllKeywordNotificationByMemberId(memberId);

        return keywordNotifications.stream()
            .map(KeywordNotificationResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}