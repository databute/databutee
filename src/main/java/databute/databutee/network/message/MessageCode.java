package databute.databutee.network.message;

public enum MessageCode {

    REGISTER(0);

    private final int value;

    MessageCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
