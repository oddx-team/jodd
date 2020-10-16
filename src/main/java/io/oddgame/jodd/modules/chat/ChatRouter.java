package io.oddgame.jodd.modules.chat;

import com.jinyframework.core.bio.HttpRouter;
import lombok.val;

public class ChatRouter {
    public static HttpRouter getRouter() {
        val router = new HttpRouter();
        router.get("/", ChatHandler::getChats);
        return router;
    }
}
