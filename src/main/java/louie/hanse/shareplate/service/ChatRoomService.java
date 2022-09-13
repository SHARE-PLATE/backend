package louie.hanse.shareplate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Chat;
import louie.hanse.shareplate.domain.ChatLog;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.repository.ChatLogRepository;
import louie.hanse.shareplate.repository.ChatRepository;
import louie.hanse.shareplate.repository.ChatRoomRepository;
import louie.hanse.shareplate.type.ChatRoomType;
import louie.hanse.shareplate.web.dto.chatroom.ChatRoomDetailResponse;
import louie.hanse.shareplate.web.dto.chatroom.ChatRoomListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatLogRepository chatLogRepository;
    private final ChatRepository chatRepository;
    private final MemberService memberService;
    private final ShareService shareService;

    @Transactional
    public ChatRoomDetailResponse getDetail(Long id, Long memberId) {
        ChatRoom chatRoom = findByIdOrElseThrow(id);
        Member member = memberService.findByIdOrElseThrow(memberId);
        Optional<ChatLog> chatLogOptional = chatLogRepository.findByMemberIdAndChatRoomId(
            memberId, id);
        if (chatLogOptional.isPresent()) {
            ChatLog chatLog = chatLogOptional.get();
            chatLog.updateRecentReadDatetime();
        } else {
            ChatLog chatLog = new ChatLog(member, chatRoom);
            chatLogRepository.save(chatLog);
        }
        return new ChatRoomDetailResponse(chatRoom, member);
    }

    public List<ChatRoomListResponse> getList(Long memberId, ChatRoomType type) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMemberId(memberId, type);
        List<ChatRoomListResponse> chatRoomListResponses = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            int unreadCount = chatRepository.getUnread(memberId, chatRoom.getId());
            Optional<Chat> optionalChat = chatRepository
                .findTopByChatRoomIdOrderByWrittenDateTimeDesc(chatRoom.getId());

            optionalChat.ifPresentOrElse(
                chat -> chatRoomListResponses.add(
                    new ChatRoomListResponse(chatRoom, chat, unreadCount, memberId)),
                () -> chatRoomListResponses.add(
                    new ChatRoomListResponse(chatRoom, unreadCount, memberId))
            );
        }
        return chatRoomListResponses;
    }

    public ChatRoom findByIdOrElseThrow(Long id) {
        return chatRoomRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Long createQuestionChatRoom(Long memberId, Long shareId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findByIdOrElseThrow(shareId);
        ChatRoom chatRoom = new ChatRoom(member, share, ChatRoomType.QUESTION);
        chatRoom.addChatRoomMember(share.getWriter());
        return chatRoomRepository.save(chatRoom).getId();
    }
}
