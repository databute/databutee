package databute.databutee.network;

import com.google.common.collect.Maps;
import databute.databutee.network.message.MessageCode;
import databute.databutee.network.message.MessageCodeResolver;
import databute.databutee.network.message.MessageDeserializer;
import databute.databutee.network.message.MessageSerializer;
import databute.databutee.network.message.codec.MessageToPacketEncoder;
import databute.databutee.network.message.codec.PacketToMessageDecoder;
import databute.databutee.network.packet.codec.ByteToPacketDecoder;
import databute.databutee.network.packet.codec.PacketToByteEncoder;
import databute.databutee.network.register.RegisterMessageSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterSessionConnector {

    private SocketChannel channel;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;

    private final EventLoopGroup loopGroup;
    private final MessageCodeResolver resolver;
    private final Map<MessageCode, MessageSerializer> serializers;
    private final Map<MessageCode, MessageDeserializer> deserializers;

    public DatabuterSessionConnector(EventLoopGroup loopGroup) {
        this.loopGroup = checkNotNull(loopGroup, "loopGroup");
        this.resolver = new MessageCodeResolver();

        this.serializers = Maps.newHashMap();
        this.serializers.put(MessageCode.REGISTER, new RegisterMessageSerializer());

        this.deserializers = Maps.newHashMap();
    }

    public InetSocketAddress remoteAddress() {
        return remoteAddress;
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

                        pipeline.addLast(new PacketToByteEncoder());
                        pipeline.addLast(new ByteToPacketDecoder());

                        pipeline.addLast(new MessageToPacketEncoder(serializers));
                        pipeline.addLast(new PacketToMessageDecoder(resolver, deserializers));

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
