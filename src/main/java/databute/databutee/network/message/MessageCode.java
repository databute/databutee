package databute.databutee.network.message;

public enum MessageCode {

    REGISTER(0),
    NODE_NOTIFICATION(1),
    BUCKET_NOTIFICATION(2),
    ENTITY_REQUEST(3);

    private final int value;

    MessageCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
