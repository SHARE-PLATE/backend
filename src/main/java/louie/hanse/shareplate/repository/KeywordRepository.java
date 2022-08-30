package louie.hanse.shareplate.repository;

import java.util.List;
import louie.hanse.shareplate.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findByMemberId(Long memberId);
}
