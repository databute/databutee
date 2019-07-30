package databute.databutee.entity.expire;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntityMessageSerializer implements MessageSerializer<ExpireEntityMessage> {

    @Override
    public Packet serialize(ExpireEntityMessage expireEntityMessage) {
        checkNotNull(expireEntityMessage, "expireEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(expireEntityMessage.id());
        packet.writeString(expireEntityMessage.key());
        packet.writeLong(expireEntityMessage.expirationTimestamp().toEpochMilli());
        return packet;
    }
}
