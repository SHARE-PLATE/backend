package louie.hanse.shareplate.repository;

import louie.hanse.shareplate.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    boolean existsByMemberIdAndShareId(Long memberId, Long shareId);

    void deleteByMemberIdAndShareId(Long memberId, Long shareId);
}
