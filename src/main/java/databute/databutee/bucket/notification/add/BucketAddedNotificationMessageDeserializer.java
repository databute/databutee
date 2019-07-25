package databute.databutee.bucket.notification.add;

import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.packet.Packet;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketAddedNotificationMessageDeserializer implements MessageDeserializer<BucketAddedNotificationMessage> {

    @Override
    public BucketAddedNotificationMessage deserialize(Packet packet) {
        checkNotNull(packet, "packet");

        final String id = packet.readString();
        final String activeNodeId = packet.readString();
        final String standbyNodeId = packet.readString();
        return new BucketAddedNotificationMessage(id, activeNodeId, standbyNodeId);
    }
}
