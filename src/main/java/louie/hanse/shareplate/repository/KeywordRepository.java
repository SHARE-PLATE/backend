package louie.hanse.shareplate.repository;

import java.util.List;
import java.util.Optional;
import louie.hanse.shareplate.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KeywordRepository extends JpaRepository<Keyword, Long>, CustomKeywordRepository {

    @Query("select k from Keyword k "
        + "join k.member m on m.id <> :memberId "
        + "where :title like concat('%', k.contents, '%') and "
        + "(6371 * acos( cos( radians(:latitude) ) * cos( radians( k.latitude ) ) * "
        + "cos( radians( k.longitude ) - radians(:longitude) ) + "
        + "sin( radians(:latitude) ) * sin( radians( k.latitude ) ) ) ) <= 2")
    List<Keyword> findAllByContainsContentsAndNotMemberIdAndAroundShare(
        @Param("memberId") Long memberId, @Param("title") String title,
        @Param("longitude") double longitude, @Param("latitude") double latitude);

    void deleteAllByMemberIdAndLocation(Long memberId, String location);

    List<Keyword> findAllByMemberIdAndLocation(Long memberId, String location);

    @Query("select k from Keyword k join fetch k.member where k.id = :id")
    Optional<Keyword> findWithMemberById(@Param("id") Long id);

    boolean existsByMemberIdAndContentsAndLocation(Long memberId, String contents, String location);

    boolean existsByMemberIdAndLocation(Long memberId, String location);

}
