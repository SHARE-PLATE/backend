package louie.hanse.shareplate.repository;

import java.util.Optional;
import louie.hanse.shareplate.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShareRepository extends JpaRepository<Share, Long>, CustomShareRepository {

    boolean existsByIdAndWriterId(Long id, Long writerId);

    @Query("select s from Share s "
        + "join fetch s.writer "
        + "where s.id = :id")
    Optional<Share> findWithWriterById(@Param("id") Long id);

    @Query("select s from Share s "
        + "join fetch s.shareImages "
        + "where s.id = :id")
    Optional<Share> findWithShareImageByIdOrElseThrow(@Param("id") Long id);
}
