package databute.databutee.entity;

import databute.databutee.network.message.Message;

public interface EntityMessage extends Message {

    String id();

    String key();

}
