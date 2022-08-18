package louie.hanse.shareplate.service;

import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.repository.ChatRoomRepository;
import louie.hanse.shareplate.web.dto.chatroom.ChatRoomDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    public ChatRoomDetailResponse getDetail(Long id, Long memberId) {
        ChatRoom chatRoom = findByIdOrElseThrow(id);
        Member member = memberService.findByIdOrElseThrow(memberId);
        return new ChatRoomDetailResponse(chatRoom, member);
    }

    public ChatRoom findByIdOrElseThrow(Long id) {
        return chatRoomRepository.findById(id).orElseThrow();
    }

}
