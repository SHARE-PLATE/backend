package louie.hanse.shareplate.repository;

import louie.hanse.shareplate.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, CustomChatRepository {

}
