package databute.databutee.bucket;

import com.google.common.collect.Maps;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class BucketGroup {

    private final Map<String, Bucket> buckets;

    public BucketGroup() {
        this.buckets = Maps.newConcurrentMap();
    }

    public Bucket find(String id) {
        return buckets.get(id);
    }

    public boolean add(Bucket bucket) {
        checkNotNull(bucket, "bucket");

        return (buckets.putIfAbsent(bucket.id(), bucket) == null);
    }

    public Bucket remove(String id) {
        checkNotNull(id, "id");

        return buckets.remove(id);
    }
}
