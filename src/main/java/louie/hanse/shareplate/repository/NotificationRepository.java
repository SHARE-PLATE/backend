package louie.hanse.shareplate.repository;

import java.util.List;
import louie.hanse.shareplate.domain.ActivityNotification;
import louie.hanse.shareplate.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from ActivityNotification n where n.member.id = :memberId and n.type = 'ACTIVITY'")
    List<ActivityNotification> findAllByMemberId(@Param("memberId") Long memberId);

}
