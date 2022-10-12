package louie.hanse.shareplate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Chat;
import louie.hanse.shareplate.domain.ChatLog;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.ChatRoomMember;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.ChatRoomExceptionType;
import louie.hanse.shareplate.repository.ChatLogRepository;
import louie.hanse.shareplate.repository.ChatRepository;
import louie.hanse.shareplate.repository.ChatRoomMemberRepository;
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
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatLogRepository chatLogRepository;
    private final ChatRepository chatRepository;
    private final MemberService memberService;
    private final ShareService shareService;

    @Transactional
    public ChatRoomDetailResponse getDetail(Long id, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Optional<ChatRoomMember> chatRoomMemberOptional = chatRoomMemberRepository
            .findByChatRoomIdAndMemberId(id, memberId);
        if (chatRoomMemberOptional.isEmpty()) {
            throw new GlobalException(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND);
        }
        ChatRoomMember chatRoomMember = chatRoomMemberOptional.get();
        ChatRoom chatRoom = chatRoomMember.getChatRoom();
        Optional<ChatLog> chatLogOptional = chatLogRepository.findByMemberIdAndChatRoomId(
            memberId, id);
        if (chatLogOptional.isPresent()) {
            ChatLog chatLog = chatLogOptional.get();
            chatLog.updateRecentReadDatetime();
        } else {
            ChatLog chatLog = new ChatLog(member, chatRoom);
            chatLogRepository.save(chatLog);
        }
        return new ChatRoomDetailResponse(chatRoomMember, member);
    }

    public List<ChatRoomListResponse> getList(Long memberId, ChatRoomType type) {
        memberService.findByIdOrElseThrow(memberId);
        List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository
            .findAllByMemberIdAndChatRoomType(memberId, type);
        List<ChatRoomListResponse> chatRoomListResponses = new ArrayList<>();
        for (ChatRoomMember chatRoomMember : chatRoomMembers) {
            ChatRoom chatRoom = chatRoomMember.getChatRoom();
            int unreadCount = chatRepository.getUnread(memberId, chatRoom.getId());
            Optional<Chat> optionalChat = chatRepository
                .findTopByChatRoomIdOrderByWrittenDateTimeDesc(chatRoom.getId());

            optionalChat.ifPresentOrElse(
                chat -> chatRoomListResponses.add(
                    new ChatRoomListResponse(chatRoomMember, chat, unreadCount, memberId)),
                () -> chatRoomListResponses.add(
                    new ChatRoomListResponse(chatRoomMember, unreadCount, memberId))
            );
        }
        return chatRoomListResponses;
    }

    public ChatRoom findByIdOrElseThrow(Long id) {
        return chatRoomRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ChatRoomExceptionType.CHAT_ROOM_NOT_FOUND));
    }

    @Transactional
    public Long createQuestionChatRoom(Long memberId, Long shareId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = shareService.findByIdOrElseThrow(shareId);
        if (share.isWriter(member)) {
            new GlobalException(ChatRoomExceptionType.WRITER_CAN_NOT_QUESTION_CHAT);
        }
        ChatRoom chatRoom = new ChatRoom(member, share, ChatRoomType.QUESTION);
        chatRoom.addChatRoomMember(share.getWriter());
        return chatRoomRepository.save(chatRoom).getId();
    }

    public Long findIdByShareIdAndType(Long shareId, ChatRoomType type) {
        ChatRoom chatRoom = chatRoomRepository.findByShareIdAndType(shareId, type);
        return chatRoom.getId();
    }
}
