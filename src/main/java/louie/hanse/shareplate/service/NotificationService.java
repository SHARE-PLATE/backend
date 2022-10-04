package louie.hanse.shareplate.service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ActivityNotification;
import louie.hanse.shareplate.domain.Entry;
import louie.hanse.shareplate.domain.Keyword;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Notification;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.NotificationExceptionType;
import louie.hanse.shareplate.repository.EntryRepository;
import louie.hanse.shareplate.repository.KeywordRepository;
import louie.hanse.shareplate.repository.NotificationRepository;
import louie.hanse.shareplate.type.ActivityType;
import louie.hanse.shareplate.type.NotificationType;
import louie.hanse.shareplate.web.dto.notification.ActivityNotificationResponse;
import louie.hanse.shareplate.web.dto.notification.KeywordNotificationResponse;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private static final ZoneOffset seoulZoneOffset = ZoneOffset.of("+9");

    private final EntryRepository entryRepository;
    private final NotificationRepository notificationRepository;
    private final KeywordRepository keywordRepository;
    private final ShareService shareService;
    private final MemberService memberService;
    private final TaskScheduler taskScheduler;
    private final MessageSendingOperations messageSendingOperations;

    public List<ActivityNotificationResponse> getActivityNotificationList(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);
        List<ActivityNotification> activityNotifications = notificationRepository
            .findAllActivityNotificationByMemberId(memberId);

        return activityNotifications.stream()
            .map(ActivityNotificationResponse::new)
            .collect(Collectors.toList());
    }

    public List<KeywordNotificationResponse> getKeywordNotificationList(Long memberId) {
        memberService.findByIdOrElseThrow(memberId);
        List<Notification> keywordNotifications = notificationRepository
            .findAllKeywordNotificationByMemberId(memberId);

        return keywordNotifications.stream()
            .map(KeywordNotificationResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Notification notification = findWithMemberByIdOrElseThrow(id);
        notification.isNotMemberThrowException(member);
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(List<Long> idList, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        for (Long id : idList) {
            Notification notification = findWithMemberByIdOrElseThrow(id);
            notification.isNotMemberThrowException(member);
        }
        notificationRepository.deleteAllByIdInBatch(idList);
    }

    @Transactional
    public void saveKeywordNotificationAndSend(Long shareId, Long memberId) {
        Share share = shareService.findByIdOrElseThrow(shareId);
        double longitude = share.getLongitude();
        double latitude = share.getLatitude();
        List<Keyword> keywords = keywordRepository.findAllByContainsContentsAndNotMemberIdAndAroundShare(
            memberId, share.getTitle(), longitude - 2, longitude + 2,
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

        sendKeywordNotifications(keywords, responses);

        createDeadlineNotificationSchedule(share.getId());
    }

    @Transactional
    public void saveActivityNotificationAndSend(Long shareId, Long memberId, ActivityType activityType) {
        Share share = shareService.findByIdOrElseThrow(shareId);
        Member member = memberService.findByIdOrElseThrow(memberId);
        List<Entry> entries = entryRepository.findAllByShareIdAndMemberId(shareId,
            memberId);

        List<ActivityNotification> activityNotifications = new ArrayList<>();
        List<ActivityNotificationResponse> responses = new ArrayList<>();
        for (Entry entry : entries) {
            ActivityNotification activityNotification = new ActivityNotification(share,
                entry.getMember(), NotificationType.ACTIVITY, activityType, member);
            activityNotifications.add(activityNotification);
            responses.add(new ActivityNotificationResponse(activityNotification));
        }
        notificationRepository.saveAll(activityNotifications);

        for (int i = 0; i < entries.size(); i++) {
            messageSendingOperations.convertAndSend(
                "/queue/notifications/entries/" + entries.get(i).getId(), responses.get(i));
        }
    }

    private void createDeadlineNotificationSchedule(Long shareId) {
        Share share = shareService.findByIdOrElseThrow(shareId);

        Instant instant = share.getClosedDateTime().minusMinutes(30)
            .toInstant(seoulZoneOffset);

        taskScheduler.schedule(() -> {
            List<Entry> entries = entryRepository.findAllByShareId(shareId);

            List<ActivityNotification> activityNotifications = new ArrayList<>();
            List<ActivityNotificationResponse> activityNotificationResponses = new ArrayList<>();
            for (Entry entry : entries) {
                ActivityNotification activityNotification = new ActivityNotification(
                    share, entry.getMember(), NotificationType.ACTIVITY, ActivityType.DEADLINE);
                activityNotificationResponses.add(
                    new ActivityNotificationResponse(activityNotification));
            }
            notificationRepository.saveAll(activityNotifications);

            for (int i = 0; i < entries.size(); i++) {
                messageSendingOperations.convertAndSend(
                    "/queue/notifications/entries/" + entries.get(i).getId(),
                    activityNotificationResponses.get(i));
            }
        }, instant);
    }

    private void sendKeywordNotifications(List<Keyword> keywords,
        List<KeywordNotificationResponse> responses) {
        for (int i = 0; i < keywords.size(); i++) {
            Long keywordId = keywords.get(i).getId();
            messageSendingOperations.convertAndSend(
                "/queue/notifications/keywords/" + keywordId, responses.get(i));
        }
    }

    private Notification findWithMemberByIdOrElseThrow(Long id) {
        return notificationRepository.findWithMemberById(id).orElseThrow(
            () -> new GlobalException(NotificationExceptionType.NOTIFICATION_NOT_FOUND));
    }
}
