package louie.hanse.shareplate.converter;

import louie.hanse.shareplate.type.ChatRoomType;
import org.springframework.core.convert.converter.Converter;

public class StringToChatRoomTypeConverter implements Converter<String, ChatRoomType> {

    @Override
    public ChatRoomType convert(String source) {
        return ChatRoomType.valueOfWithCaseInsensitive(source);
    }
}
