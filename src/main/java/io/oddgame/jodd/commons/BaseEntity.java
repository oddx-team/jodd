package io.oddgame.jodd.commons;

import com.google.gson.annotations.SerializedName;
import org.bson.types.ObjectId;

public abstract class BaseEntity {
    protected ObjectId id;
    @SerializedName("_id")
    protected String objectId;

    public void setId(ObjectId id) {
        this.id = id;
        this.objectId = id.toString();
    }

    public ObjectId getId() {
        return id;
    }

    public String getObjectId() {
        return objectId;
    }
}
