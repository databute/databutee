package databute.databutee.entity.expire;

import com.google.common.base.MoreObjects;
import databute.databutee.entity.EntityKey;
import databute.databutee.entity.EntityMessage;
import databute.databutee.network.message.MessageCode;

import java.time.Instant;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExpireEntityMessage implements EntityMessage {

    private final String id;
    private final EntityKey key;
    private final Instant expirationTimestamp;

    public ExpireEntityMessage(EntityKey key, Instant expirationTimestamp) {
        this.id = UUID.randomUUID().toString();
        this.key = checkNotNull(key, "key");
        this.expirationTimestamp = expirationTimestamp;
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.EXPIRE_ENTITY;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key.key();
    }

    public Instant expirationTimestamp() {
        return expirationTimestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("expirationTimestamp", expirationTimestamp)
                .toString();
    }
}
