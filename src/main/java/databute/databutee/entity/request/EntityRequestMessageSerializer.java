package databute.databutee.entity.request;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityRequestMessageSerializer implements MessageSerializer<EntityRequestMessage> {

    @Override
    public Packet serialize(EntityRequestMessage entityRequestMessage) {
        checkNotNull(entityRequestMessage, "entityRequestMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(entityRequestMessage.requestType().name());
        packet.writeString(entityRequestMessage.key());
        packet.writeString(entityRequestMessage.valueType().name());
        switch (entityRequestMessage.valueType()) {
            case INTEGER: {
                final Integer value = (Integer) entityRequestMessage.value();
                packet.writeInt(value);
                break;
            }
            case LONG: {
                final Long value = (Long) entityRequestMessage.value();
                packet.writeLong(value);
                break;
            }
            case STRING: {
                final String value = (String) entityRequestMessage.value();
                packet.writeString(value);
                break;
            }
        }
        return packet;
    }
}
