package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.ChatRoomMember;
import louie.hanse.shareplate.domain.Entry;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.repository.ChatRoomMemberRepository;
import louie.hanse.shareplate.repository.EntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class EntryService {

    private final MemberService memberService;
    private final ShareService shareService;
    private final EntryRepository entryRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public void entry(Long shareId, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findByIdOrElseThrow(shareId);

        Entry entry = new Entry(share, member);
        entryRepository.save(entry);
        ChatRoom chatRoom = share.getChatRoom();
        ChatRoomMember chatRoomMember = new ChatRoomMember(member, chatRoom);
        chatRoomMemberRepository.save(chatRoomMember);

    }

    @Transactional
    public void cancel(Long shareId, Long memberId) {
        entryRepository.deleteByMemberIdAndShareId(memberId, shareId);
    }
}
