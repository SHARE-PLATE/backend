package louie.hanse.shareplate.type;

public enum ChatRoomType {
    QUESTION, ENTRY;

    public static ChatRoomType valueOfWithCaseInsensitive(String name) {
        name = name.toUpperCase();
        return valueOf(name);
    }
}
