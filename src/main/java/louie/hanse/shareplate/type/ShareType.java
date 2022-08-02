package louie.hanse.shareplate.type;

public enum ShareType {
    DELIVERY, INGREDIENT;

    public static ShareType valueOfWithCaseInsensitive(String name) {
        name = name.toUpperCase();
        return ShareType.valueOf(name);
    }
}
