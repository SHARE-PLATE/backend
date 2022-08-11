package louie.hanse.shareplate.repository;

import louie.hanse.shareplate.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

    boolean existsByMemberIdAndShareId(Long memberId, Long shareId);

    void deleteByMemberIdAndShareId(Long memberId, Long shareId);
}
