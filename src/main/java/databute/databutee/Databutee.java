package databute.databutee;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import databute.databutee.cluster.DatabuterNodeGroup;
import databute.databutee.network.DatabuterSession;
import databute.databutee.network.DatabuterSessionConnector;
import databute.databutee.network.register.RegisterMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

public class Databutee {

    private static final Logger logger = LoggerFactory.getLogger(Databutee.class);

    private final DatabuteeConfiguration configuration;
    private final DatabuterNodeGroup databuterNodeGroup;

    public Databutee(DatabuteeConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
        this.databuterNodeGroup = new DatabuterNodeGroup();
    }

    public DatabuterNodeGroup databuterNodeGroup() {
        return databuterNodeGroup;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration)
                .add("databuterNodeGroup", databuterNodeGroup)
                .toString();
    }
}
