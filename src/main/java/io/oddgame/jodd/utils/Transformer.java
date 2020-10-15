package io.oddgame.jodd.utils;

import com.google.gson.Gson;
import io.oddgame.jodd.factories.AppFactory;
import lombok.AllArgsConstructor;

public class Transformer {
    public static Gson gson = AppFactory.getGson();
    public static String transform(Object o) {
        if (o instanceof String) {
            o = new MessageResponse(o.toString());
        }

        return gson.toJson(o);
    }

    @AllArgsConstructor
    public static class MessageResponse {
        private final String message;
    }
}
