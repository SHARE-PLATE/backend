package louie.hanse.shareplate.repository;

import static louie.hanse.shareplate.domain.QChat.chat;
import static louie.hanse.shareplate.domain.QChatLog.chatLog;
import static louie.hanse.shareplate.domain.QChatRoom.chatRoom;
import static louie.hanse.shareplate.domain.QChatRoomMember.chatRoomMember;
import static louie.hanse.shareplate.domain.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

@RequiredArgsConstructor
public class CustomChatRepositoryImpl implements CustomChatRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public int getUnread(Long memberId) {
        List<Long> chatRoomIds = queryFactory
            .select(chatRoom.id)
            .from(chatRoom)
            .join(chatRoom.chatRoomMembers, chatRoomMember)
            .join(chatRoomMember.member, member)
            .on(member.id.eq(memberId))
            .fetch();

        int count = 0;
        for (Long chatRoomId : chatRoomIds) {
            LocalDateTime localDateTime = queryFactory
                .select(chatLog.recentReadDatetime)
                .from(chatLog)
                .where(
                    chatLog.chatRoom.id.eq(chatRoomId),
                    chatLog.member.id.eq(memberId)
                ).fetchOne();

            count += queryFactory
                .select(chat.count())
                .from(chat)
                .where(
                    chat.chatRoom.id.eq(chatRoomId),
                    chat.writer.id.ne(memberId),
                    gtWrittenDateTime(localDateTime)
                ).fetchOne()
                .intValue();
        }
        return count;
    }

    private BooleanExpression gtWrittenDateTime(LocalDateTime localDateTime) {
        return ObjectUtils.isEmpty(localDateTime) ? null : chat.writtenDateTime.gt(localDateTime);
    }
}
