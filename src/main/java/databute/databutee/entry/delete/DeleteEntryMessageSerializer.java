package databute.databutee.entry.delete;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class DeleteEntityMessageSerializer implements MessageSerializer<DeleteEntityMessage> {

    @Override
    public Packet serialize(DeleteEntityMessage deleteEntityMessage) {
        checkNotNull(deleteEntityMessage, "deleteEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(deleteEntityMessage.id());
        packet.writeString(deleteEntityMessage.key());
        return packet;
    }
}
