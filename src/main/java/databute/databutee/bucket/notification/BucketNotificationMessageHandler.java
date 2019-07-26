package databute.databutee.bucket.notification;

import databute.databutee.bucket.Bucket;
import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketNotificationMessageHandler extends MessageHandler<BucketNotificationMessage> {

    private static final Logger logger = LoggerFactory.getLogger(BucketNotificationMessageHandler.class);

    public BucketNotificationMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(BucketNotificationMessage bucketNotificationMessage) {
        switch (bucketNotificationMessage.type()) {
            case ADDED:
                addBucket(bucketNotificationMessage);
                break;
            case UPDATED:
                updateBucket(bucketNotificationMessage);
                break;
            case REMOVED:
                removeBucket(bucketNotificationMessage);
                break;
        }
    }

    private void addBucket(BucketNotificationMessage bucketNotificationMessage) {
        final Bucket bucket = new Bucket(bucketNotificationMessage.id())
                .factor(bucketNotificationMessage.factor())
                .activeNodeId(bucketNotificationMessage.activeNodeId())
                .standbyNodeId(bucketNotificationMessage.standbyNodeId());
        final boolean added = session().databutee().bucketGroup().add(bucket);
        if (added) {
            logger.debug("Added bucket {}", bucket);
        }
    }

    private void updateBucket(BucketNotificationMessage bucketNotificationMessage) {
        final Bucket bucket = session().databutee().bucketGroup().find(bucketNotificationMessage.id());
        if (bucket == null) {
            logger.error("Failed to update bucket from notification message {}", bucketNotificationMessage);
        } else {
            bucket.update(bucketNotificationMessage);
            logger.debug("Updated bucket {}", bucket);
        }
    }

    private void removeBucket(BucketNotificationMessage bucketNotificationMessage) {
        final Bucket bucket = session().databutee().bucketGroup().remove(bucketNotificationMessage.id());
        if (bucket == null) {
            logger.error("Failed to remove bucket from notification message {}", bucketNotificationMessage);
        } else {
            logger.debug("Removed bucket {}", bucket);
        }
    }
}
