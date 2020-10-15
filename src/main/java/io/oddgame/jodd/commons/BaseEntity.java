package io.oddgame.jodd.commons;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public abstract class BaseEntity {
    protected ObjectId id;
}
