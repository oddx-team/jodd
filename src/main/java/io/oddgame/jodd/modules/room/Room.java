package io.oddgame.jodd.modules.room;

import lombok.*;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
    private ObjectId id;
    private String name;
    private String host;
    private int size;
    private int guess;
    private Status status;
    private String country;

    public enum Status {
        NOT_STARTED,
        PLAYING
    }
}
