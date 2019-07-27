package databute.databutee.entity.get;

import com.google.common.base.MoreObjects;
import databute.databutee.entity.EntityKey;
import databute.databutee.entity.EntityMessage;
import databute.databutee.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetEntityMessage implements EntityMessage {

    private final EntityKey key;

    public GetEntityMessage(EntityKey key) {
        this.key = checkNotNull(key, "key");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.GET_ENTITY;
    }

    @Override
    public EntityKey key() {
        return key;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("key", key)
                .toString();
    }
}
