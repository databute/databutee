package databute.databutee.bucket.notification.add;

import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketAddedNotificationMessageHandler extends MessageHandler<BucketAddedNotificationMessage> {

    private static final Logger logger = LoggerFactory.getLogger(BucketAddedNotificationMessageHandler.class);

    public BucketAddedNotificationMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(BucketAddedNotificationMessage bucketAddedNotification) {
        logger.debug("Handling bucket added notification message {}", bucketAddedNotification);
    }
}
