package databute.databutee.cluster.remove;

import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class RemoveClusterNodeMessageDeserializer implements MessageDeserializer<RemoveClusterNodeMessage> {

    @Override
    public RemoveClusterNodeMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        return new RemoveClusterNodeMessage(id);
    }
}
