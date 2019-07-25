package databute.databutee.cluster.remove;

import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveClusterNodeMessageHandler extends MessageHandler<RemoveClusterNodeMessage> {

    private static final Logger logger = LoggerFactory.getLogger(RemoveClusterNodeMessageHandler.class);

    public RemoveClusterNodeMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(RemoveClusterNodeMessage removeClusterNodeMessage) {
        logger.debug("Handling remove cluster node message {}", removeClusterNodeMessage);

        session().databutee().databuterNodeGroup().remove(removeClusterNodeMessage.id());
    }
}
