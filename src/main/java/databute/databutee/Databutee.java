package databute.databutee;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import databute.databutee.bucket.Bucket;
import databute.databutee.bucket.BucketGroup;
import databute.databutee.entity.EmptyEntityKeyException;
import databute.databutee.entity.EntityKey;
import databute.databutee.entity.EntityMessage;
import databute.databutee.entity.UnsupportedValueTypeException;
import databute.databutee.entity.delete.DeleteEntityMessage;
import databute.databutee.entity.expire.ExpireEntityMessage;
import databute.databutee.entity.get.GetEntityMessage;
import databute.databutee.entity.set.SetEntityMessage;
import databute.databutee.entity.update.UpdateEntityMessage;
import databute.databutee.network.DatabuterSession;
import databute.databutee.network.DatabuterSessionConnector;
import databute.databutee.network.register.RegisterMessage;
import databute.databutee.node.DatabuterNode;
import databute.databutee.node.DatabuterNodeGroup;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

public class Databutee {

    private static final Logger logger = LoggerFactory.getLogger(Databutee.class);

    private final DatabuteeConfiguration configuration;
    private final DatabuterNodeGroup databuterNodeGroup;
    private final BucketGroup bucketGroup;
    private final Dispatcher dispatcher;

    public Databutee(DatabuteeConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
        this.databuterNodeGroup = new DatabuterNodeGroup();
        this.bucketGroup = new BucketGroup();
        this.dispatcher = new Dispatcher();
    }

    public DatabuteeConfiguration configuration() {
        return configuration;
    }

    public DatabuterNodeGroup databuterNodeGroup() {
        return databuterNodeGroup;
    }

    public BucketGroup bucketGroup() {
        return bucketGroup;
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public void connect() throws ConnectException {
        final List<InetSocketAddress> addresses = Lists.newArrayList(configuration.addresses());
        while (!addresses.isEmpty()) {
            final int index = RandomUtils.nextInt(0, addresses.size());
            final InetSocketAddress address = addresses.remove(index);
            final DatabuterSessionConnector connector = new DatabuterSessionConnector(this, configuration.loopGroup());

            try {
                final DatabuterSession session = connector.connect(address).get();
                logger.info("Connected with server at {}", connector.remoteAddress());

                session.send(new RegisterMessage());
                return;
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Failed to connect to {}.", address);
            }
        }

        throw new ConnectException();
    }

    public void get(String key, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final GetEntityMessage getEntityMessage = new GetEntityMessage(entityKey);
        sendEntityMessage(getEntityMessage, callback);
    }

    public void set(String key, Object value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        if (value instanceof Integer) {
            final Integer integerValue = (Integer) value;
            setInteger(key, integerValue, callback);
        } else if (value instanceof Long) {
            final Long longValue = (Long) value;
            setLong(key, longValue, callback);
        } else if (value instanceof String) {
            final String stringValue = (String) value;
            setString(key, stringValue, callback);
        } else {
            throw new UnsupportedValueTypeException();
        }
    }

    public void setInteger(String key, Integer value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final SetEntityMessage setEntityMessage = SetEntityMessage.setInteger(entityKey, value);
        sendEntityMessage(setEntityMessage, callback);
    }

    public void setLong(String key, Long value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final SetEntityMessage setEntityMessage = SetEntityMessage.setLong(entityKey, value);
        sendEntityMessage(setEntityMessage, callback);
    }

    public void setString(String key, String value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final SetEntityMessage setEntityMessage = SetEntityMessage.setString(entityKey, value);
        sendEntityMessage(setEntityMessage, callback);
    }

    public void setList(String key, List<String> value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final SetEntityMessage setEntityMessage = SetEntityMessage.setList(entityKey, value);
        sendEntityMessage(setEntityMessage, callback);
    }

    public void setSet(String key, Set<String> value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final SetEntityMessage setEntityMessage = SetEntityMessage.setSet(entityKey, value);
        sendEntityMessage(setEntityMessage, callback);
    }

    public void setDictionary(String key, Map<String, String> value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final SetEntityMessage setEntityMessage = SetEntityMessage.setDictionary(entityKey, value);
        sendEntityMessage(setEntityMessage, callback);
    }

    public void update(String key, Object value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        if (value instanceof Integer) {
            final Integer integerValue = (Integer) value;
            updateInteger(key, integerValue, callback);
        } else if (value instanceof Long) {
            final Long longValue = (Long) value;
            updateLong(key, longValue, callback);
        } else if (value instanceof String) {
            final String stringValue = (String) value;
            updateString(key, stringValue, callback);
        } else {
            final String toStringValue = value.toString();
            updateString(key, toStringValue, callback);
        }
    }

    public void updateInteger(String key, Integer value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final UpdateEntityMessage updateEntityMessage = UpdateEntityMessage.updateInteger(entityKey, value);
        sendEntityMessage(updateEntityMessage, callback);
    }

    public void updateLong(String key, Long value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final UpdateEntityMessage updateEntityMessage = UpdateEntityMessage.updateLong(entityKey, value);
        sendEntityMessage(updateEntityMessage, callback);
    }

    public void updateString(String key, String value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final UpdateEntityMessage updateEntityMessage = UpdateEntityMessage.updateString(entityKey, value);
        sendEntityMessage(updateEntityMessage, callback);
    }

    public void updateList(String key, List<String> value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final UpdateEntityMessage updateEntityMessage = UpdateEntityMessage.updateList(entityKey, value);
        sendEntityMessage(updateEntityMessage, callback);
    }

    public void updateSet(String key, Set<String> value, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final UpdateEntityMessage updateEntityMessage = UpdateEntityMessage.updateSet(entityKey, value);
        sendEntityMessage(updateEntityMessage, callback);
    }

    public void updateDictionary(String key, Map<String, String> value, Callback callback)
            throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(value, "value");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final UpdateEntityMessage updateEntityMessage = UpdateEntityMessage.updateDictionary(entityKey, value);
        sendEntityMessage(updateEntityMessage, callback);
    }

    public void delete(String key, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final DeleteEntityMessage deleteEntityMessage = new DeleteEntityMessage(entityKey);
        sendEntityMessage(deleteEntityMessage, callback);
    }

    public void expire(String key, Instant expireAt, Callback callback) throws EmptyEntityKeyException {
        checkNotNull(key, "key");
        checkNotNull(callback, "callback");

        final EntityKey entityKey = new EntityKey(key);
        final ExpireEntityMessage expireEntityMessage = new ExpireEntityMessage(entityKey, expireAt);
        sendEntityMessage(expireEntityMessage, callback);
    }

    private void sendEntityMessage(EntityMessage entityMessage, Callback callback) {
        final int count = bucketGroup.count();
        final HashCode hashKey = Hashing.crc32().hashString(entityMessage.key(), StandardCharsets.UTF_8);
        final int keyFactor = Hashing.consistentHash(hashKey, count);
        final Bucket bucket = bucketGroup.findByKeyFactor(keyFactor);
        if (bucket == null) {
            logger.error("Failed to find bucket by keyFactor {}.", keyFactor);
        } else {
            final DatabuterNode activeNode = bucket.activeNode();
            if (activeNode != null) {
                dispatcher.enqueue(entityMessage.id(), callback);
                activeNode.session().send(entityMessage);
                logger.debug("keyFactor: {}, bucket: {}, active node: {}", keyFactor, bucket.id(), activeNode.id());
            } else {
                final DatabuterNode standbyNode = bucket.standbyNode();
                if (standbyNode != null) {
                    dispatcher.enqueue(entityMessage.id(), callback);
                    standbyNode.session().send(entityMessage);
                    logger.debug("keyFactor: {}, bucket: {}, standby node: {}", keyFactor, bucket.id(), standbyNode.id());
                } else {
                    logger.error("Bucket {} does not assigned to any node.", bucket.id());
                }
            }
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration)
                .add("databuterNodeGroup", databuterNodeGroup)
                .add("bucketGroup", bucketGroup)
                .toString();
    }
}
