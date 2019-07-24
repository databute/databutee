package databute.databutee.network.message;

import databute.databutee.network.DatabuterSession;

public interface MessageHandler<S extends DatabuterSession, M extends Message> {

    S session();

    void handle(M message);

}
