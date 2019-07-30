package databute.databutee.entry.result.success;

import databute.databutee.Callback;
import databute.databutee.Databutee;
import databute.databutee.Dispatcher;
import databute.databutee.entry.EmptyEntityKeyException;
import databute.databutee.entry.Entity;
import databute.databutee.entry.EntityKey;
import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class EntityOperationSuccessMessageHandler extends MessageHandler<EntityOperationSuccessMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EntityOperationSuccessMessageHandler.class);

    public EntityOperationSuccessMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(EntityOperationSuccessMessage entityOperationSuccessMessage) {
        final Databutee databutee = session().databutee();
        final Dispatcher dispatcher = databutee.dispatcher();

        final String id = entityOperationSuccessMessage.id();
        final Callback callback = dispatcher.dequeue(id);
        if (callback == null) {
            logger.error("No callback found by id {}", id);
        } else {
            try {
                final EntityKey entityKey = new EntityKey(entityOperationSuccessMessage.key());
                final Object value = entityOperationSuccessMessage.value();
                final Instant createdTimestamp = entityOperationSuccessMessage.createdTimestamp();
                final Instant lastUpdatedTimestamp = entityOperationSuccessMessage.lastUpdatedTimestamp();
                final Entity entity = new Entity(entityKey, value, createdTimestamp, lastUpdatedTimestamp);
                callback.onSuccess(entity);
            } catch (EmptyEntityKeyException e) {
                callback.onFailure(e);
            }
        }
    }
}
