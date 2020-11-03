package io.oddgame.jodd.websocket;

import io.oddgame.jodd.utils.JWTUtils;
import lombok.val;
import org.java_websocket.handshake.ClientHandshake;

import java.util.concurrent.ThreadLocalRandom;

public class WSHandshake {
    public static String handshake(ClientHandshake req) throws Exception {
        if (!req.hasFieldValue("Cookie")) {
            val isProd = System.getProperty("isProd");
            if (isProd == null) {
                val randomId = ThreadLocalRandom.current().nextInt();
                return String.valueOf(randomId);
            }
            throw new Exception("Not accepted!");
        }
        val cookieHeader = req.getFieldValue("Cookie");
        val token = cookieHeader.replace("token=", "");
        try {
            val decoded = JWTUtils.validateJWT(token);
            return decoded.getClaim("userid").asString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
