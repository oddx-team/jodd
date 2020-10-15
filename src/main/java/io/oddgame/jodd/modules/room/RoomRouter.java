package io.oddgame.jodd.modules.room;

import com.jinyframework.core.bio.HttpRouter;
import lombok.val;

public class RoomRouter {
    public static HttpRouter getRouter() {
        val router = new HttpRouter();
        router.get("/:country", RoomHandler::getRooms);
        router.post("/:country", RoomHandler::createRoom);
        router.get("/id/:id", RoomHandler::getRoom);
        return router;
    }
}
