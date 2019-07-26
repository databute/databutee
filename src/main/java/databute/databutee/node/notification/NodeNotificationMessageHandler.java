package databute.databutee.node.notification;

import databute.databutee.network.DatabuterSession;
import databute.databutee.network.message.MessageHandler;
import databute.databutee.node.DatabuterNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeNotificationMessageHandler extends MessageHandler<NodeNotificationMessage> {

    private static final Logger logger = LoggerFactory.getLogger(NodeNotificationMessageHandler.class);

    public NodeNotificationMessageHandler(DatabuterSession session) {
        super(session);
    }

    @Override
    public void handle(NodeNotificationMessage nodeNotificationMessage) {
        switch (nodeNotificationMessage.type()) {
            case ADDED:
                addNode(nodeNotificationMessage);
                break;
            case REMOVED:
                removeNode(nodeNotificationMessage);
                break;
        }
    }

    private void addNode(NodeNotificationMessage nodeNotificationMessage) {
        final DatabuterNode node = DatabuterNode.builder()
                .id(nodeNotificationMessage.id())
                .address(nodeNotificationMessage.address())
                .port(nodeNotificationMessage.port())
                .build();
        final boolean added = session().databutee().databuterNodeGroup().add(node);
        if (added) {
            logger.debug("Added databuter node {}", node);
        }
    }

    private void removeNode(NodeNotificationMessage nodeNotificationMessage) {
        final DatabuterNode node = session().databutee().databuterNodeGroup().remove(nodeNotificationMessage.id());
        if (node == null) {
            logger.error("Failed to remove node from notification message {}", nodeNotificationMessage);
        } else {
            logger.debug("Removed node {}", node);
        }
    }
}
