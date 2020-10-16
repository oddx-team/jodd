package io.oddgame.jodd.websocket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Attachment {
    private String userId;
    private String nickname;
    private String inRoom;
}
