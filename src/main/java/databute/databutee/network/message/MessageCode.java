package databute.databutee.network.message;

public enum MessageCode {

    REGISTER(0),
    NODE_NOTIFICATION(1),
    BUCKET_NOTIFICATION(2),
    GET_ENTITY(3),
    SET_ENTITY(4),
    UPDATE_ENTITY(5),
    DELETE_ENTITY(6);

    private final int value;

    MessageCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
