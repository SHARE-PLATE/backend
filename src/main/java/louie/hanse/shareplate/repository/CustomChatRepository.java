package louie.hanse.shareplate.repository;

public interface CustomChatRepository {

    int getTotalUnread(Long memberId);

    int getUnread(Long memberId, Long chatRoomId);
}
