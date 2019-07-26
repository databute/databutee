package databute.databutee.bucket.notification;

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
        logger.debug("Handling bucket notification message {}", bucketNotificationMessage);
    }
}
