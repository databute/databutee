package databute.databutee;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import databute.databutee.bucket.Bucket;
import databute.databutee.bucket.BucketGroup;
import databute.databutee.entity.request.EntityRequestMessage;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

public class Databutee {

    private static final Logger logger = LoggerFactory.getLogger(Databutee.class);

    private final DatabuteeConfiguration configuration;
    private final DatabuterNodeGroup databuterNodeGroup;
    private final BucketGroup bucketGroup;

    public Databutee(DatabuteeConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
        this.databuterNodeGroup = new DatabuterNodeGroup();
        this.bucketGroup = new BucketGroup();
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

    @Beta
    public void query(EntityRequestMessage entityRequestMessage) {
        final int count = bucketGroup.count();
        final HashCode hashedKey = Hashing.crc32().hashString(entityRequestMessage.key(), StandardCharsets.UTF_8);
        final int factor = Hashing.consistentHash(hashedKey, count);
        final Bucket bucket = bucketGroup.findByFactor(factor);
        if (bucket == null) {
            logger.error("Failed to find bucket by factor {}.", factor);
        } else {
            final DatabuterNode activeNode = bucket.activeNode();
            if (activeNode != null) {
                activeNode.session().send(entityRequestMessage);
                logger.debug("factor: {}, bucket: {}, active node: {}", factor, bucket.id(), activeNode.id());
            } else {
                final DatabuterNode standbyNode = bucket.standbyNode();
                if (standbyNode != null) {
                    standbyNode.session().send(entityRequestMessage);
                    logger.debug("factor: {}, bucket: {}, standby node: {}", factor, bucket.id(), standbyNode.id());
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
