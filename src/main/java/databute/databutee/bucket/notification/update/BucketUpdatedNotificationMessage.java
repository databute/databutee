package databute.databutee.bucket.notification.update;

import com.google.common.base.MoreObjects;
import databute.databutee.network.message.Message;
import databute.databutee.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketUpdatedNotificationMessage implements Message {

    private final String id;
    private final String activeNodeId;
    private final String standbyNodeId;

    public BucketUpdatedNotificationMessage(String id, String activeNodeId, String standbyNodeId) {
        this.id = checkNotNull(id, "id");
        this.activeNodeId = activeNodeId;
        this.standbyNodeId = standbyNodeId;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.BUCKET_UPDATED_NOTIFICATION;
    }

    public String id() {
        return id;
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
                .add("id", id)
                .add("activeNodeId", activeNodeId)
                .add("standbyNodeId", standbyNodeId)
                .toString();
    }
}
