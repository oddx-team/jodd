package io.oddgame.jodd.modules.room;

import io.oddgame.jodd.commons.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room extends BaseEntity {
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
