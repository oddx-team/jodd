package io.oddgame.jodd.factories;

import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import io.oddgame.jodd.configs.MongoDB;
import lombok.Setter;

@Setter
public class AppFactory {
    private static Gson gson;
    private static MongoDatabase mongoDatabase;

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static MongoDatabase getMongoDatabase() {
        if (mongoDatabase == null) {
            mongoDatabase = MongoDB.getMongoDatabase();
        }
        return mongoDatabase;
    }
}
