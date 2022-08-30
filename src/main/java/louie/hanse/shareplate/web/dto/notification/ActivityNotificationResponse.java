package louie.hanse.shareplate.web.dto.notification;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import louie.hanse.shareplate.domain.ActivityNotification;
import louie.hanse.shareplate.type.ActivityType;

@ToString
@Getter
public class ActivityNotificationResponse {

    private String recruitmentMemberNickname;
    private LocalDateTime notificationCreatedDateTime;
    private String shareTitle;
    private String shareThumbnailImageUrl;
    private Long shareId;
    private ActivityType activityType;

    public ActivityNotificationResponse(ActivityNotification activityNotification) {
        this.recruitmentMemberNickname = activityNotification.getActivityType().equals(ActivityType.ENTRY) ?
                activityNotification.getEntryMember().getNickname() : null;
        this.notificationCreatedDateTime = activityNotification.getCreatedDateTime();
        this.shareTitle = activityNotification.getShare().getTitle();
        this.shareThumbnailImageUrl = activityNotification.getShare().getShareImages().get(0)
            .getImageUrl();
        this.shareId = activityNotification.getShare().getId();
        this.activityType = activityNotification.getActivityType();
    }
}
