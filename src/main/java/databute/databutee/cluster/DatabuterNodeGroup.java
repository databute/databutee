package databute.databutee.cluster;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabuterNodeGroup {

    private static final Logger logger = LoggerFactory.getLogger(DatabuterNodeGroup.class);

    private final Map<String, DatabuterNode> nodes;

    public DatabuterNodeGroup() {
        this.nodes = Maps.newConcurrentMap();
    }

    public boolean add(DatabuterNode node) {
        checkNotNull(node, "node");

        final boolean added = (nodes.putIfAbsent(node.id(), node) == null);
        if (added) {
            if (logger.isDebugEnabled()) {
                logger.debug("Added databuter node {}", node);
            } else {
                logger.info("Added databuter node {}", node.id());
            }
        }

        return added;
    }

    public boolean remove(DatabuterNode node) {
        return remove(checkNotNull(node, "node").id());
    }

    public boolean remove(String id) {
        checkNotNull(id, "id");

        final DatabuterNode node = nodes.remove(id);
        final boolean removed = (node != null);
        if (removed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Removed databuter node {}", node);
            } else {
                logger.info("Removed databuter node {}", node.id());
            }
        }

        return removed;
    }
}
