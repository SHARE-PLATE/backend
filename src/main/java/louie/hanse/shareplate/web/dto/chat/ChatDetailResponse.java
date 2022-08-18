package louie.hanse.shareplate.web.dto.chat;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import louie.hanse.shareplate.domain.Chat;
import louie.hanse.shareplate.domain.Member;

@NoArgsConstructor
@Getter
public class ChatDetailResponse {

    private String contents;
    private String writer;
    private String writerThumbnailImageUrl;
    private LocalDateTime writtenDateTime;
    private boolean writtenByMe;

    public ChatDetailResponse(Chat chat, Member member) {
        this.contents = chat.getContents();
        this.writer = chat.getWriter().getNickname();
        this.writerThumbnailImageUrl = chat.getWriter().getThumbnailImageUrl();
        this.writtenDateTime = chat.getWrittenDateTime();
        this.writtenByMe = member.equals(chat.getWriter());
    }
}
