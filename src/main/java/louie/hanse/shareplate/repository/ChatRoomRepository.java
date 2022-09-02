package louie.hanse.shareplate.repository;

import java.util.List;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.type.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select c from ChatRoom c " +
        "join c.chatRoomMembers m on m.member.id = :memberId "
        + "where c.type = :type")
    List<ChatRoom> findAllByMemberId(@Param("memberId") Long memberId,
        @Param("type") ChatRoomType type);
}
