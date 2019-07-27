package databute.databutee.bucket;

import com.google.common.base.MoreObjects;
import databute.databutee.bucket.notification.BucketNotificationMessage;
import databute.databutee.node.DatabuterNode;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class Bucket {

    private int factor;
    private String activeNodeId;
    private DatabuterNode activeNode;
    private String standbyNodeId;
    private DatabuterNode standbyNode;

    private final String id;

    public Bucket(String id) {
        this.id = checkNotNull(id, "id");
    }

    public String id() {
        return id;
    }

    public int factor() {
        return factor;
    }

    public Bucket factor(int factor) {
        this.factor = factor;
        return this;
    }

    public String activeNodeId() {
        return activeNodeId;
    }

    public Bucket activeNodeId(String activeNodeId) {
        this.activeNodeId = activeNodeId;
        return this;
    }

    public DatabuterNode activeNode() {
        return activeNode;
    }

    public Bucket activeNode(DatabuterNode activeNode) {
        this.activeNode = activeNode;
        return this;
    }

    public String standbyNodeId() {
        return standbyNodeId;
    }

    public Bucket standbyNodeId(String standbyNodeId) {
        this.standbyNodeId = standbyNodeId;
        return this;
    }

    public DatabuterNode standbyNode() {
        return standbyNode;
    }

    public Bucket standbyNode(DatabuterNode standbyNode) {
        this.standbyNode = standbyNode;
        return this;
    }

    public boolean update(BucketNotificationMessage bucketNotificationMessage) {
        checkNotNull(bucketNotificationMessage, "bucketNotificationMessage");

        boolean updated = false;

        if (factor != bucketNotificationMessage.factor()) {
            this.factor = bucketNotificationMessage.factor();
            updated = true;
        }

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
                .add("factor", factor)
                .add("activeNodeId", activeNodeId)
                .add("standbyNodeId", standbyNodeId)
                .toString();
    }
}
