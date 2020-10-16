package io.oddgame.jodd.modules.chat;

import io.oddgame.jodd.commons.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat extends BaseEntity {
    private String nickname;
    private String message;
    private long time;
}
