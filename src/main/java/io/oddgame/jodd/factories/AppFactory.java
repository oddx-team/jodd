package io.oddgame.jodd.factories;

import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jinyframework.core.AbstractRequestBinder.RequestTransformer;
import com.mongodb.client.MongoDatabase;
import io.oddgame.jodd.configs.MongoDB;
import io.oddgame.jodd.utils.Transformer;
import lombok.Setter;
import lombok.val;

import com.google.gson.ExclusionStrategy;

@Setter
public class AppFactory {
    private static Gson gson;
    private static RequestTransformer requestTransformer;
    private static MongoDatabase mongoDatabase;

    public static Gson getGson() {
        if (gson == null) {
            val strategy = new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getName().equals("id");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            };
            gson = new GsonBuilder()
                    .addSerializationExclusionStrategy(strategy)
                    .create();
        }
        return gson;
    }

    public static RequestTransformer getRequestTransformer() {
        if (requestTransformer == null) {
            requestTransformer = Transformer::transform;
        }
        return requestTransformer;
    }

    public static MongoDatabase getMongoDatabase() {
        if (mongoDatabase == null) {
            mongoDatabase = MongoDB.getMongoDatabase();
        }
        return mongoDatabase;
    }
}
