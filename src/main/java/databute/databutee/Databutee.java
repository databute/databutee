package databute.databutee;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

public class Databutee {

    private final DatabuteeConfiguration configuration;

    public Databutee(DatabuteeConfiguration configuration) {
        this.configuration = checkNotNull(configuration, "configuration");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("configuration", configuration)
                .toString();
    }
}
