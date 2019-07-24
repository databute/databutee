package databute.databutee.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabuterChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DatabuterChannelHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        logger.debug("Active new channel {}", channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final SocketChannel channel = (SocketChannel) ctx.channel();
        logger.debug("Inactive channel {}", channel);
    }
}
