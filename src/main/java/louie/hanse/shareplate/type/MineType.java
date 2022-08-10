package louie.hanse.shareplate.type;

public enum MineType {
    ENTRY, WRITER, WISH;

    public static MineType valueOfWithCaseInsensitive(String name) {
        name = name.toUpperCase();
        return valueOf(name);
    }

    public boolean isEntry() {
        return this == ENTRY;
    }

    public boolean isWriter() {
        return this == WRITER;
    }

    public boolean isWish() {
        return this == WISH;
    }
}
