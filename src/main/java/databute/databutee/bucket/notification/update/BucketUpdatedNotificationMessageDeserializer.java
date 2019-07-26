package databute.databutee.bucket.notification.update;

import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketUpdatedNotificationMessageDeserializer implements MessageDeserializer<BucketUpdatedNotificationMessage> {

    @Override
    public BucketUpdatedNotificationMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String activeNodeId = packet.readString();
        final String standbyNodeId = packet.readString();
        return new BucketUpdatedNotificationMessage(id, activeNodeId, standbyNodeId);
    }
}
