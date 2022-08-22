package louie.hanse.shareplate.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ChatLog;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.repository.ChatLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatLogService {

    private final ChatLogRepository chatLogRepository;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    @Transactional
    public void updateRecentReadDateTime(Long memberId, Long chatRoomId) {
        Optional<ChatLog> chatLogOptional = chatLogRepository.findByMemberIdAndChatRoomId(
            memberId, chatRoomId);
        if (chatLogOptional.isPresent()) {
            chatLogOptional.get().updateRecentReadDatetime();
        } else {
            Member member = memberService.findByIdOrElseThrow(memberId);
            ChatRoom chatRoom = chatRoomService.findByIdOrElseThrow(chatRoomId);
            chatLogRepository.save(new ChatLog(member, chatRoom));
        }
    }
}
