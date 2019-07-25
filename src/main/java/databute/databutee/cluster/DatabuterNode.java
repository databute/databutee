package databute.databutee.cluster;

import com.google.common.base.MoreObjects;

public class DatabuterNode {

    private final String id;
    private final String address;
    private final int port;

    public DatabuterNode(String id, String address, int port) {
        this.id = id;
        this.address = address;
        this.port = port;
    }

    public String id() {
        return id;
    }

    public String address() {
        return address;
    }

    public int port() {
        return port;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("address", address)
                .add("port", port)
                .toString();
    }
}
