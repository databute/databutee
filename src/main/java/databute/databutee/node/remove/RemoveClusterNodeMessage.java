package databute.databutee.node.remove;

import com.google.common.base.MoreObjects;
import databute.databutee.network.message.Message;
import databute.databutee.network.message.MessageCode;

public class RemoveClusterNodeMessage implements Message {

    private final String id;

    public RemoveClusterNodeMessage(String id) {
        this.id = id;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.REMOVE_CLUSTER_NODE;
    }

    public String id() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .toString();
    }
}
