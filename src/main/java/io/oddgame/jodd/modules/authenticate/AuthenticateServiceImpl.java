package io.oddgame.jodd.modules.authenticate;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.oddgame.jodd.factories.AppFactory;
import io.oddgame.jodd.utils.JWTUtils;
import lombok.val;

import java.util.Date;
import java.util.Objects;

public class AuthenticateServiceImpl implements AuthenticateService {
    private final MongoCollection<User> userCollection = AppFactory.getMongoDatabase()
            .getCollection("user", User.class);

    public AuthenticateServiceImpl() {
        val indexOptions = new IndexOptions().unique(true);
        userCollection.createIndex(Indexes.ascending("email"), indexOptions);
    }

    @Override
    public String enterGame(String nickname, String email) {
        if (email == null) email = "guess" + new Date().getTime() + "@oddgame.io";
        val insertResult = userCollection.insertOne(new User(nickname, email));
        val userId = Objects.requireNonNull(insertResult.getInsertedId()).asObjectId().getValue().toString();
        return JWTUtils.createJWT(userId, nickname);
    }
}
