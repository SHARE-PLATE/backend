package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Chat;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @Transactional
    public Chat save(Long chatRoomId, Long memberId, String contents) {
        //TODO 추후 커스텀 예외처리 적용
        ChatRoom chatRoom = chatRoomService.findByIdOrElseThrow(chatRoomId);
        Member member = memberService.findByIdOrElseThrow(memberId);
        Chat chat = new Chat(chatRoom, member, contents);
        return chatRepository.save(chat);
    }

    public int getTotalUnread(Long memberId) {
        return chatRepository.getTotalUnread(memberId);
    }
}
