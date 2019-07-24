package databute.databutee.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabuterChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DatabuterChannelHandler.class);

    private DatabuterSession session;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        session = new DatabuterSession(channel);
        logger.debug("Active new databuter session {}", session);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.debug("Inactive databuter session {}", session);
    }
}
