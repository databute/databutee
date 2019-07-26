package databute.databutee.bucket;

import com.google.common.base.MoreObjects;
import databute.databutee.bucket.notification.BucketNotificationMessage;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class Bucket {

    private String activeNodeId;
    private String standbyNodeId;

    private final String id;

    public Bucket(String id) {
        this.id = checkNotNull(id, "id");
    }

    public String id() {
        return id;
    }

    public String activeNodeId() {
        return activeNodeId;
    }

    public Bucket activeNodeId(String activeNodeId) {
        this.activeNodeId = activeNodeId;
        return this;
    }

    public String standbyNodeId() {
        return standbyNodeId;
    }

    public Bucket standbyNodeId(String standbyNodeId) {
        this.standbyNodeId = standbyNodeId;
        return this;
    }

    public boolean update(BucketNotificationMessage bucketNotificationMessage) {
        checkNotNull(bucketNotificationMessage, "bucketNotificationMessage");

        boolean updated = false;

        if (!StringUtils.equals(activeNodeId, bucketNotificationMessage.activeNodeId())) {
            this.activeNodeId = bucketNotificationMessage.activeNodeId();
            updated = true;
        }

        if (!StringUtils.equals(standbyNodeId, bucketNotificationMessage.standbyNodeId())) {
            this.standbyNodeId = bucketNotificationMessage.standbyNodeId();
            updated = true;
        }

        return updated;
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
