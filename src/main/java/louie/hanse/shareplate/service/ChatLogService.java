package louie.hanse.shareplate.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ChatLog;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.repository.ChatLogRepository;
import louie.hanse.shareplate.repository.ChatRoomMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatLogService {

    private final ChatLogRepository chatLogRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    @Transactional
    public void updateRecentReadDateTime(Long memberId, Long chatRoomId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        ChatRoom chatRoom = chatRoomService.findByIdOrElseThrow(chatRoomId);

        boolean isExistChatRoomMember = chatRoomMemberRepository.existsByMemberIdAndChatRoomId(memberId, chatRoomId);
        if (!isExistChatRoomMember) {
            throw new GlobalException(ChatRoomExceptionType.CHAT_ROOM_NOT_JOINED);
        }

        Optional<ChatLog> chatLogOptional = chatLogRepository.findByMemberIdAndChatRoomId(
            memberId, chatRoomId);
        if (chatLogOptional.isPresent()) {
            chatLogOptional.get().updateRecentReadDatetime();
        } else {
            chatLogRepository.save(new ChatLog(member, chatRoom));
        }
    }
}
