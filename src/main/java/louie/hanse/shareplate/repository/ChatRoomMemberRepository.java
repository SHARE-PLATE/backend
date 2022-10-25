package louie.hanse.shareplate.repository;

import java.util.List;
import java.util.Optional;
import louie.hanse.shareplate.domain.ChatRoomMember;
import louie.hanse.shareplate.type.ChatRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("select crm from ChatRoomMember crm "
        + "join fetch crm.chatRoom cr "
        + "join fetch cr.share s "
        + "where crm.chatRoom.id = :chatRoomId and "
        + "crm.member.id = :memberId")
    ChatRoomMember findWithShareByChatRoomIdAndMemberId(
        @Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);

    void deleteByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    List<ChatRoomMember> findAllByMemberId(Long memberId);

    @Query("select crm from ChatRoomMember crm "
        + "join crm.chatRoom cr on cr.type = :type "
        + "where crm.member.id = :memberId ")
    List<ChatRoomMember> findAllByMemberIdAndChatRoomType(
        @Param("memberId") Long memberId, @Param("type") ChatRoomType type);

    @Query("select crm from ChatRoomMember crm "
        + "join fetch crm.chatRoom cr "
        + "left join fetch cr.chats c "
        + "left join fetch c.writer cw "
        + "join fetch cr.share s "
        + "join fetch s.writer sw "
        + "where crm.chatRoom.id = :chatRoomId and "
        + "crm.member.id = :memberId "
        + "order by c.writtenDateTime ")
    Optional<ChatRoomMember> findWithChatRoomAndShareByChatRoomIdAndMemberId(
        @Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);

    boolean existsByMemberIdAndChatRoomId(Long memberId, Long chatRoomId);

    void deleteByMemberIdAndChatRoomId(Long memberId, Long chatRoomId);

    @Query("select crm from ChatRoomMember crm "
        + "where crm.chatRoom.id in (:chatRoomIds) "
        + "and crm.member.id = :memberId")
    Optional<ChatRoomMember> findByChatRoomIdsAndMemberId(
        @Param("chatRoomIds") List<Long> chatRoomIds, @Param("memberId") Long memberId);
}
