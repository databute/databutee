package databute.databutee.entry.get;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetEntityMessageSerializer implements MessageSerializer<GetEntityMessage> {

    @Override
    public Packet serialize(GetEntityMessage getEntityMessage) {
        checkNotNull(getEntityMessage, "getEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(getEntityMessage.id());
        packet.writeString(getEntityMessage.key());
        return packet;
    }
}
