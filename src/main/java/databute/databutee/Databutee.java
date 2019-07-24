package databute.databutee;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import databute.databutee.network.DatabuterSessionConnector;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public class Databutee {

    private final DatabuteeConfiguration configuration;

    public Databutee(DatabuteeConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
    }

    public CompletableFuture<Void> connect() {
        final List<CompletableFuture<Void>> futures = Lists.newArrayList();

        for (InetSocketAddress address : configuration.addresses()) {
            final DatabuterSessionConnector connector = new DatabuterSessionConnector(configuration.loopGroup());
            futures.add(connector.connect(address));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration)
                .toString();
    }
}
