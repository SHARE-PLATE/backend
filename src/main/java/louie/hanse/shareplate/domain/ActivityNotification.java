package louie.hanse.shareplate.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import louie.hanse.shareplate.type.ActivityType;

@Entity
public class ActivityNotification extends Notification {

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member entryMember;
}
