package databute.databutee.entry.result.fail;

import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationFailMessageDeserializer implements MessageDeserializer<EntityOperationFailMessage> {

    @Override
    public EntityOperationFailMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String key = packet.readString();
        final EntityOperationErrorCode errorCode = EntityOperationErrorCode.valueOf(packet.readString());
        return new EntityOperationFailMessage(id, key, errorCode);
    }
}
