package io.oddgame.jodd.modules.authenticate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.oddgame.jodd.factories.AppFactory;
import lombok.val;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        val secret = Algorithm.HMAC256("secret");
        val expire = Instant.now().plus(1, ChronoUnit.DAYS);
        val expireDate = Date.from(expire);
        return JWT.create()
                .withClaim("userid",
                        Objects.requireNonNull(insertResult.getInsertedId()).asObjectId().getValue().toString())
                .withClaim("nickname", nickname)
                .withIssuer("jodd")
                .withExpiresAt(expireDate)
                .sign(secret);
    }
}
