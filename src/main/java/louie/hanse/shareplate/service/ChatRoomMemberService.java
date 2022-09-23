package louie.hanse.shareplate.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ChatRoomMember;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.repository.ChatRoomMemberRepository;
import louie.hanse.shareplate.repository.EntryRepository;
import louie.hanse.shareplate.web.dto.chatRoomMember.ChatRoomMemberListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final EntryRepository entryRepository;
    private final MemberService memberService;

    @Transactional
    public void exitChatRoom(Long chatRoomId, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        ChatRoomMember chatRoomMember = chatRoomMemberRepository
            .findWithShareByChatRoomIdAndMemberId(chatRoomId, memberId);
        Share share = chatRoomMember.getChatRoom().getShare();

        share.isWriterAndIsNotCancelThrowException(member);
        chatRoomMemberRepository.deleteByChatRoomIdAndMemberId(chatRoomId, memberId);

        if (share.isNotEnd()) {
            entryRepository.deleteByMemberIdAndShareId(memberId, share.getId());
        }
    }

    public List<ChatRoomMemberListResponse> getChatRoomMemberList(Long memberId) {
        return chatRoomMemberRepository
            .findAllByMemberId(memberId).stream()
            .map(ChatRoomMemberListResponse::new)
            .collect(Collectors.toList());
    }
}
