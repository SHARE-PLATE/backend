package louie.hanse.shareplate.repository;

import louie.hanse.shareplate.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, Long> {
}
