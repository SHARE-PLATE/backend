package louie.hanse.shareplate.repository;

import java.util.List;
import louie.hanse.shareplate.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShareRepository extends JpaRepository<Share, Long>, CustomShareRepository {

    List<Share> findByWriterId(Long memberId);

    @Query("select s from Share s "
        + "join s.entries e on e.member.id = :memberId ")
    List<Share> findWithEntry(@Param("memberId") Long memberId);

    @Query("select s from Share s "
        + "join s.wishList w on w.member.id = :memberId ")
    List<Share> findWithWish(@Param("memberId") Long memberId);
}
