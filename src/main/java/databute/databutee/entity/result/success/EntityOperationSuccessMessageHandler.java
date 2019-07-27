package databute.databutee.entity.result.success;

import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityOperationSuccessMessageHandler extends MessageHandler<EntityOperationSuccessMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntityOperationSuccessMessageHandler.class);

    public EntityOperationSuccessMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(EntityOperationSuccessMessage entityOperationSuccessMessage) {
        logger.debug("Handling entity operaion success message {}", entityOperationSuccessMessage);
    }
}
