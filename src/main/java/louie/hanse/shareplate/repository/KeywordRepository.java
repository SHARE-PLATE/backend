package louie.hanse.shareplate.repository;

import java.util.List;
import java.util.Optional;
import louie.hanse.shareplate.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KeywordRepository extends JpaRepository<Keyword, Long>, CustomKeywordRepository {

    List<Keyword> findByMemberId(Long memberId);

    @Query("select k from Keyword k "
        + "join k.member m "
        + "where :title like concat('%', k.contents, '%') and "
        + "k.longitude between :longitude1 and :longitude2 and "
        + "k.latitude between :latitude1 and :latitude2 and "
        + "m.id <> :memberId")
    List<Keyword> findAllByContainsContentsAndAroundShareV2(
        @Param("memberId") Long memberId, @Param("title") String title,
        @Param("longitude1") double longitude1, @Param("longitude2") double longitude2,
        @Param("latitude1") double latitude1, @Param("latitude2") double latitude2);

    void deleteAllByMemberIdAndLocation(Long memberId, String location);

    List<Keyword> findAllByMemberIdAndLocation(Long memberId, String location);

    @Query("select k from Keyword k join fetch k.member where k.id = :id")
    Optional<Keyword> findWithMemberById(@Param("id") Long id);

}
