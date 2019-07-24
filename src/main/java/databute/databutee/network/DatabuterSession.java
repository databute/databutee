package databute.databutee.network;

import com.google.common.base.MoreObjects;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterSession {

    private final SocketChannel channel;
    private final InetSocketAddress localAddress;
    private final InetSocketAddress remoteAddress;

    public DatabuterSession(SocketChannel channel) {
        this.channel = checkNotNull(channel, "channel");
        this.localAddress = checkNotNull(channel.localAddress(), "localAddress");
        this.remoteAddress = checkNotNull(channel.remoteAddress(), "remoteAddress");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("channel", channel)
                .toString();
    }
}
