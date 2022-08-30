package louie.hanse.shareplate.web.dto.notification;

import java.time.LocalDateTime;
import lombok.Getter;
import louie.hanse.shareplate.domain.Notification;

@Getter
public class KeywordNotificationResponse {

    private String shareLocation;
    private Long shareId;
    private String shareTitle;
    private String shareThumbnailImageUrl;
    private LocalDateTime notificationCreatedDateTime;

    public KeywordNotificationResponse(Notification notification) {
        this.shareLocation = notification.getShare().getLocation();
        this.shareId = notification.getShare().getId();
        this.shareTitle = notification.getShare().getTitle();
        this.shareThumbnailImageUrl = notification.getShare().getShareImages().get(0).getImageUrl();
        this.notificationCreatedDateTime = notification.getCreatedDateTime();
    }
}
