package databute.databutee.bucket.notification;

import com.google.common.base.MoreObjects;
import databute.databutee.network.message.Message;
import databute.databutee.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketNotificationMessage implements Message {

    private final BucketNotificationType type;
    private final String id;
    private final int factor;
    private final String activeNodeId;
    private final String standbyNodeId;

    public BucketNotificationMessage(BucketNotificationType type,
                                     String id,
                                     int factor,
                                     String activeNodeId,
                                     String standbyNodeId) {
        this.type = checkNotNull(type, "type");
        this.id = checkNotNull(id, "id");
        this.factor = factor;
        this.activeNodeId = checkNotNull(activeNodeId, "activeNodeId");
        this.standbyNodeId = checkNotNull(standbyNodeId, "standbyNodeId");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.BUCKET_NOTIFICATION;
    }

    public BucketNotificationType type() {
        return type;
    }

    public String id() {
        return id;
    }

    public int factor() {
        return factor;
    }

    public String activeNodeId() {
        return activeNodeId;
    }

    public String standbyNodeId() {
        return standbyNodeId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("type", type)
                .add("id", id)
                .add("factor", factor)
                .add("activeNodeId", activeNodeId)
                .add("standbyNodeId", standbyNodeId)
                .toString();
    }
}
