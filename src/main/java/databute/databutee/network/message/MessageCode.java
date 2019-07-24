package databute.databutee.network.message;

public enum MessageCode {

    ;

    private final int value;

    MessageCode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
