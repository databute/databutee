package databute.databutee.node.add;

import com.google.common.base.MoreObjects;
import databute.databutee.network.message.Message;
import databute.databutee.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddClusterNodeMessage implements Message {

    private final String id;
    private final String address;
    private final int port;

    public AddClusterNodeMessage(String id, String address, int port) {
        this.id = checkNotNull(id, "id");
        this.address = checkNotNull(address, "address");
        this.port = port;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.ADD_CLUSTER_NODE;
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
                .add("messageCode", messageCode())
                .add("id", id)
                .add("address", address)
                .add("port", port)
                .toString();
    }
}
