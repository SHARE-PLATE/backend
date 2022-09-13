package louie.hanse.shareplate.repository;

import java.util.Optional;
import louie.hanse.shareplate.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, CustomChatRepository {

    Optional<Chat> findTopByChatRoomIdOrderByWrittenDateTimeDesc(Long chatRoomId);
}
