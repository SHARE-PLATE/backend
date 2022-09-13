package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ChatRoomMember;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.repository.ChatRoomMemberRepository;
import louie.hanse.shareplate.repository.EntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final EntryRepository entryRepository;

    @Transactional
    public void exitChatRoom(Long chatRoomId, Long memberId) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository
            .findWithShareByChatRoomIdAndMemberId(chatRoomId, memberId);
        Share share = chatRoomMember.getChatRoom().getShare();

        chatRoomMemberRepository.deleteByChatRoomIdAndMemberId(chatRoomId, memberId);

        if (share.isEnd()) {
            entryRepository.deleteByMemberIdAndShareId(memberId, share.getId());
        }
    }
}
