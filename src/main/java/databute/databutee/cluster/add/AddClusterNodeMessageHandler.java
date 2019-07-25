package databute.databutee.cluster.add;

import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddClusterNodeMessageHandler extends MessageHandler<AddClusterNodeMessage> {

    private static final Logger logger = LoggerFactory.getLogger(AddClusterNodeMessageHandler.class);

    public AddClusterNodeMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(AddClusterNodeMessage addClusterNodeMessage) {
        logger.debug("Handling add cluster node messsage {}", addClusterNodeMessage);
    }
}
