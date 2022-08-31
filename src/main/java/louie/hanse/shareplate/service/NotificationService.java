package louie.hanse.shareplate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ActivityNotification;
import louie.hanse.shareplate.domain.Keyword;
import louie.hanse.shareplate.domain.Notification;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.repository.KeywordRepository;
import louie.hanse.shareplate.repository.NotificationRepository;
import louie.hanse.shareplate.type.NotificationType;
import louie.hanse.shareplate.web.dto.notification.ActivityNotificationResponse;
import louie.hanse.shareplate.web.dto.notification.KeywordNotificationResponse;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final ShareService shareService;
    private final NotificationRepository notificationRepository;
    private final KeywordRepository keywordRepository;
    private final MessageSendingOperations messageSendingOperations;

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

    @Transactional
    public void deleteAll(List<Long> idList) {
        notificationRepository.deleteAllByIdInBatch(idList);
    }

    @Transactional
    public void registerAndSend(Long shareId) {
        Share share = shareService.findByIdOrElseThrow(shareId);
        double longitude = share.getLongitude();
        double latitude = share.getLatitude();
        List<Keyword> keywords = keywordRepository.findAllByContainsContentsAndAroundShareV2(
            share.getTitle(), longitude - 2, longitude + 2,
            latitude - 2, latitude + 2);

        List<Notification> notifications = new ArrayList<>();
        List<KeywordNotificationResponse> responses = new ArrayList<>();
        for (Keyword keyword : keywords) {
            Notification notification = new Notification(
                share, keyword.getMember(), NotificationType.KEYWORD);
            notifications.add(notification);
            responses.add(new KeywordNotificationResponse(notification));
        }
        notificationRepository.saveAll(notifications);

        for (int i = 0; i < keywords.size(); i++) {
            Long keywordId = keywords.get(i).getId();
            messageSendingOperations.convertAndSend(
                "/queue/notifications/keywords/" + keywordId, responses.get(i));
        }
    }
}
