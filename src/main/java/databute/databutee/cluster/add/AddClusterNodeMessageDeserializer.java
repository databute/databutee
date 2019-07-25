package databute.databutee.cluster.add;

import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddClusterNodeMessageDeserializer implements MessageDeserializer<AddClusterNodeMessage> {

    @Override
    public AddClusterNodeMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String address = packet.readString();
        final int port = packet.readInt();
        return new AddClusterNodeMessage(id, address, port);
    }
}
