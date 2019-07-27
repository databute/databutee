package databute.databutee.entity.result.fail;

import databute.databutee.Callback;
import databute.databutee.Databutee;
import databute.databutee.Dispatcher;
import databute.databutee.entity.NotFoundException;
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
        final Databutee databutee = session().databutee();
        final Dispatcher dispatcher = databutee.dispatcher();

        final String id = entityOperationFailMessage.id();
        final Callback callback = dispatcher.dequeue(id);
        if (callback == null) {
            logger.error("No callback found by id {}", id);
        } else {
            switch (entityOperationFailMessage.errorCode()) {
                case NOT_FOUND:
                    callback.onFailure(new NotFoundException(entityOperationFailMessage.key()));
                    break;
            }
        }
    }
}
