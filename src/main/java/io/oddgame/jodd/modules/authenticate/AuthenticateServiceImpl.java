package io.oddgame.jodd.modules.authenticate;

import at.favre.lib.crypto.bcrypt.BCrypt;
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

import static com.mongodb.client.model.Filters.eq;

public class AuthenticateServiceImpl implements AuthenticateService {
    private final MongoCollection<User> userCollection = AppFactory.getMongoDatabase()
            .getCollection("user", User.class);

    public AuthenticateServiceImpl() {
        val indexOptions = new IndexOptions().unique(true);
        userCollection.createIndex(Indexes.ascending("username", "email"), indexOptions);
    }

    @Override
    public User signUp(String username, String password, String email) {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        val newUser = new User(username, hashedPassword, email);
        userCollection.insertOne(newUser);
        return newUser;
    }

    @Override
    public String login(String username, String password) throws Exception {
        val userToLogin = userCollection.find(eq("username", username)).first();
        if (userToLogin == null) throw new Exception("Invalid username");
        val result = BCrypt.verifyer().verify(password.toCharArray(), userToLogin.getPassword());
        if (result.verified) {
            val secret = Algorithm.HMAC256("secret");
            val expire = Instant.now().plus(1, ChronoUnit.HOURS);
            val expireDate = Date.from(expire);
            return JWT.create()
                    .withClaim("username", username)
                    .withIssuer("jodd")
                    .withExpiresAt(expireDate)
                    .sign(secret);
        }
        throw new Exception("Invalid password");
    }
}
