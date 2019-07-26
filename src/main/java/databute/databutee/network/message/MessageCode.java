package databute.databutee.network.message;

public enum MessageCode {

    REGISTER(0),
    ADD_CLUSTER_NODE(1),
    REMOVE_CLUSTER_NODE(2),
    BUCKET_ADDED_NOTIFICATION(3),
    BUCKET_UPDATED_NOTIFICATION(4);

    private final int value;

    MessageCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
