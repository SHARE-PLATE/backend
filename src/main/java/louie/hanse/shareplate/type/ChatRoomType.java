package louie.hanse.shareplate.type;

import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.ChatRoomExceptionType;

public enum ChatRoomType {
    QUESTION, ENTRY;

    public static ChatRoomType valueOfWithCaseInsensitive(String name) {
        name = name.toUpperCase();

        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new GlobalException(ChatRoomExceptionType.INCORRECT_TYPE_VALUE);
        }
    }

    public boolean isEntry() {
        return this == ENTRY;
    }
}
