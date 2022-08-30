package louie.hanse.shareplate.repository;

import java.util.List;
import louie.hanse.shareplate.domain.ActivityNotification;
import louie.hanse.shareplate.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select a from ActivityNotification a where a.member.id = :memberId and a.type = 'ACTIVITY'")
    List<ActivityNotification> findAllActivityNotificationByMemberId(
        @Param("memberId") Long memberId);

    @Query("select  n from Notification  n where n.member.id = :memberId and n.type = 'KEYWORD'")
    List<Notification> findAllKeywordNotificationByMemberId(@Param("memberId") Long memberId);
}
