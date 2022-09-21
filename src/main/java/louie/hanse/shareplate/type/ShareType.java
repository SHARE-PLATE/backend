package louie.hanse.shareplate.type;

import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.ShareExceptionType;

public enum ShareType {
    DELIVERY, INGREDIENT;

    public static ShareType valueOfWithCaseInsensitive(String name) {
        name = name.toUpperCase();

        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new GlobalException(ShareExceptionType.INCORRECT_TYPE_VALUE);
        }
    }
}
