package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Chat;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.ChatRoomMember;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.repository.ChatRepository;
import louie.hanse.shareplate.web.dto.chat.ChatDetailResponse;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final MessageSendingOperations messageSendingOperations;

    @Transactional
    public void saveAndResponseChatMessage(Long chatRoomId, Long memberId, String contents) {
        //TODO 추후 커스텀 예외처리 적용
        ChatRoom chatRoom = chatRoomService.findByIdOrElseThrow(chatRoomId);
        Member member = memberService.findByIdOrElseThrow(memberId);
        Chat chat = new Chat(chatRoom, member, contents);
        chatRepository.save(chat);
        for (ChatRoomMember chatRoomMember : chatRoom.getChatRoomMembers()) {
            ChatDetailResponse chatDetailResponse = new ChatDetailResponse(
                chat, chatRoomMember.getMember());
            messageSendingOperations.convertAndSend(
                "/topic/chatroom-members/" + chatRoomMember.getId(), chatDetailResponse);

        }
    }

    public int getTotalUnread(Long memberId) {
        return chatRepository.getTotalUnread(memberId);
    }
}
