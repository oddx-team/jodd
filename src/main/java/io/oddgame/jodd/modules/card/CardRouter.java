package io.oddgame.jodd.modules.card;

import com.jinyframework.core.bio.HttpRouter;
import lombok.val;

public class CardRouter {
    public static HttpRouter getRouter() {
        val router = new HttpRouter();
        router.get("/:language", CardHandler::getCards);
        router.get("/generate/:color/:size", CardHandler::generateCards);
        return router;
    }
}
