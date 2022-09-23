package louie.hanse.shareplate.type;

public enum ActivityType {
    ENTRY, DEADLINE, SHARE_CANCEL, ENTRY_CANCEL;

    public boolean isDeadLine() {
        return this == DEADLINE;
    }
}
