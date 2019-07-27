package databute.databutee.entity.set;

import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.packet.BufferedPacket;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetEntityMessageSerializer implements MessageSerializer<SetEntityMessage> {

    @Override
    public Packet serialize(SetEntityMessage setEntityMessage) {
        checkNotNull(setEntityMessage, "setEntityMessage");

        final Packet packet = new BufferedPacket();
        packet.writeString(setEntityMessage.key().key());
        packet.writeString(setEntityMessage.valueType().name());
        switch (setEntityMessage.valueType()) {
            case INTEGER: {
                final Integer integerValue = (Integer) setEntityMessage.value();
                serializeIntegerValue(packet, integerValue);
                break;
            }
            case LONG: {
                final Long longValue = (Long) setEntityMessage.value();
                serializeLongValue(packet, longValue);
                break;
            }
            case STRING: {
                final String stringValue = (String) setEntityMessage.value();
                serializeStringValue(packet, stringValue);
            }
        }
        return packet;
    }

    private void serializeIntegerValue(Packet packet, Integer integerValue) {
        packet.writeInt(integerValue);
    }

    private void serializeLongValue(Packet packet, Long longValue) {
        packet.writeLong(longValue);
    }

    private void serializeStringValue(Packet packet, String stringValue) {
        packet.writeString(stringValue);
    }
}
