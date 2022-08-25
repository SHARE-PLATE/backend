package louie.hanse.shareplate.repository;

import louie.hanse.shareplate.domain.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("select crf from ChatRoomMember crf "
        + "join fetch crf.chatRoom cr "
        + "join fetch cr.share s "
        + "where crf.chatRoom.id = :chatRoomId and "
        + "crf.member.id = :memberId")
    ChatRoomMember findWithShareByChatRoomIdAndMemberId(
        @Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);

    void deleteByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}
