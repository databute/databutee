package databute.databutee.entity.set;

import com.google.common.base.MoreObjects;
import databute.databutee.entity.EntityKey;
import databute.databutee.entity.EntityMessage;
import databute.databutee.entity.EntityValueType;
import databute.databutee.network.message.MessageCode;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class SetEntityMessage implements EntityMessage {

    public static SetEntityMessage setInteger(EntityKey key, Integer integerValue) {
        return new SetEntityMessage(key, EntityValueType.INTEGER, integerValue);
    }

    public static SetEntityMessage setLong(EntityKey key, Long longValue) {
        return new SetEntityMessage(key, EntityValueType.LONG, longValue);
    }

    public static SetEntityMessage setString(EntityKey key, String stringValue) {
        return new SetEntityMessage(key, EntityValueType.STRING, stringValue);
    }

    private final String id;
    private final EntityKey key;
    private final EntityValueType valueType;
    private final Object value;

    public SetEntityMessage(EntityKey key, EntityValueType valueType, Object value) {
        this.id = UUID.randomUUID().toString();
        this.key = checkNotNull(key, "key");
        this.valueType = checkNotNull(valueType, "valueType");
        this.value = checkNotNull(value, "value");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.SET_ENTITY;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key.key();
    }

    public EntityValueType valueType() {
        return valueType;
    }

    public Object value() {
        return value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("valueType", valueType)
                .add("value", value)
                .toString();
    }
}
