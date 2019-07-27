package databute.databutee.entity;

public class DuplicateEntityKeyException extends Exception {

    public DuplicateEntityKeyException(String key) {
        super("Found duplicated entity key " + key);
    }
}
