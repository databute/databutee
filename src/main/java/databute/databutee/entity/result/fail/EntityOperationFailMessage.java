package databute.databutee.entity.result.fail;

import com.google.common.base.MoreObjects;
import databute.databutee.entity.EntityMessage;
import databute.databutee.network.message.MessageCode;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntityOperationFailMessage implements EntityMessage {

    private final String id;
    private final String key;
    private final EntityOperationErrorCode errorCode;

    public EntityOperationFailMessage(String id, String key, EntityOperationErrorCode errorCode) {
        this.id = checkNotNull(id, "id");
        this.key = checkNotNull(key, "key");
        this.errorCode = checkNotNull(errorCode, "errorCode");
    }

    @Override
    public MessageCode messageCode() {
        return MessageCode.ENTITY_OPERATION_FAIL;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String key() {
        return key;
    }

    public EntityOperationErrorCode errorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageCode", messageCode())
                .add("id", id)
                .add("key", key)
                .add("errorCode", errorCode)
                .toString();
    }
}
