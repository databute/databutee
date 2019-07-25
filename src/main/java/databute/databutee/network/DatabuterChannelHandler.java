package databute.databutee.network;

import databute.databutee.Databutee;
import databute.databutee.cluster.add.AddClusterNodeMessageHandler;
import databute.databutee.cluster.remove.RemoveClusterNodeMessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DatabuterChannelHandler.class);

    private DatabuterSession session;

    private final Databutee databutee;
    private final CompletableFuture<DatabuterSession> connectFuture;

    public DatabuterChannelHandler(Databutee databutee, CompletableFuture<DatabuterSession> connectFuture) {
        this.databutee = databutee;
        this.connectFuture = checkNotNull(connectFuture, "connectFuture");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        session = new DatabuterSession(databutee, channel);
        connectFuture.complete(session);
        logger.debug("Active new databuter session {}", session);

        configurePipeline(ctx);
    }

    private void configurePipeline(ChannelHandlerContext ctx) {
        final ChannelPipeline pipeline = ctx.pipeline();

        pipeline.addLast(new AddClusterNodeMessageHandler(session));
        pipeline.addLast(new RemoveClusterNodeMessageHandler(session));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.debug("Inactive databuter session {}", session);
    }
}
