package databute.databutee.bucket.notification.update;

import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketUpdatedNotificationMessageHandler extends MessageHandler<BucketUpdatedNotificationMessage> {

    private static final Logger logger = LoggerFactory.getLogger(BucketUpdatedNotificationMessageHandler.class);

    public BucketUpdatedNotificationMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(BucketUpdatedNotificationMessage bucketUpdatedNotification) {
        logger.debug("Handling bucket updated notification message {}", bucketUpdatedNotification);
    }
}
