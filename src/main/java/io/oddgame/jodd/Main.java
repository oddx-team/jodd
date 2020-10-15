package io.oddgame.jodd;

import com.jinyframework.HttpServer;
import com.jinyframework.core.AbstractRequestBinder.HttpResponse;
import io.oddgame.jodd.factories.AppFactory;
import io.oddgame.jodd.middlewares.JWTMiddleware;
import io.oddgame.jodd.modules.authenticate.AuthenticateRouter;
import io.oddgame.jodd.modules.room.RoomRouter;
import io.oddgame.jodd.websocket.OddWebSocket;
import lombok.val;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        val gson = AppFactory.getGson();
        val server = HttpServer.port(1234)
                .useTransformer(gson::toJson);
        new Thread(OddWebSocket.port(1235)).start();

        server.get("/", JWTMiddleware::jwtMiddleware, ctx -> HttpResponse.of(ctx.dataParam("username")));
        server.use("/auth", AuthenticateRouter.getRouter());
        server.use("/rooms", RoomRouter.getRouter());

        server.start();
    }
}