package databute.databutee.entity.result.fail;

import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityOperationFailMessageHandler extends MessageHandler<EntityOperationFailMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntityOperationFailMessageHandler.class);

    public EntityOperationFailMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(EntityOperationFailMessage entityOperationFailMessage) {
        logger.debug("Handling entity operation fail message {}", entityOperationFailMessage);
    }
}
