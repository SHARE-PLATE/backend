package louie.hanse.shareplate.type;

public enum ActivityType {
    ENTRY, DEADLINE, SHARE_CANCEL, ENTRY_CANCEL, QUESTION_CHATROOM;

    public boolean isDeadLine() {
        return this == DEADLINE;
    }
}
