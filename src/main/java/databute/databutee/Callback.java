package databute.databutee;

import databute.databutee.entity.Entity;

public interface Callback {

    void onSuccess(Entity entity);

    void onFailure(Exception e);

}
