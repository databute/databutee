package databute.databutee.node.add;

import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import databute.databutee.node.DatabuterNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddClusterNodeMessageHandler extends MessageHandler<AddClusterNodeMessage> {

    private static final Logger logger = LoggerFactory.getLogger(AddClusterNodeMessageHandler.class);

    public AddClusterNodeMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(AddClusterNodeMessage addClusterNodeMessage) {
        logger.debug("Handling add node node messsage {}", addClusterNodeMessage);

        final DatabuterNode node = DatabuterNode.builder()
                .id(addClusterNodeMessage.id())
                .address(addClusterNodeMessage.address())
                .port(addClusterNodeMessage.port())
                .build();
        session().databutee().databuterNodeGroup().add(node);
    }
}
