package io.oddgame.jodd.modules.authenticate;

import com.jinyframework.core.bio.HttpRouter;
import lombok.val;

public class AuthenticateRouter {
    public static HttpRouter getRouter() {
        val router = new HttpRouter();
        router.post("/login", AuthenticateHandler::login);
        router.post("/signup", AuthenticateHandler::signUp);
        return router;
    }
}
