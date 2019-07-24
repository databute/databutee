package databute.databutee.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterSessionConnector {

    private SocketChannel channel;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    private final EventLoopGroup loopGroup;

    public DatabuterSessionConnector(EventLoopGroup loopGroup) {
        this.loopGroup = checkNotNull(loopGroup, "loopGroup");
    }

    public CompletableFuture<Void> connect(InetSocketAddress remoteAddress) {
        this.remoteAddress = checkNotNull(remoteAddress, "remoteAddress");

        final CompletableFuture<Void> future = new CompletableFuture<>();
        final Bootstrap bootstrap = new Bootstrap()
                .group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        final ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast(new DatabuterChannelHandler());
                    }
                });
        bootstrap.connect(remoteAddress).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                try {
                    final SocketChannel channel = (SocketChannel) channelFuture.channel();
                    DatabuterSessionConnector.this.channel = channel;
                    DatabuterSessionConnector.this.localAddress = channel.localAddress();
                    DatabuterSessionConnector.this.remoteAddress = channel.remoteAddress();

                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            } else {
                future.completeExceptionally(channelFuture.cause());
            }
        });

        return future;
    }
}
