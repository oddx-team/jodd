package io.oddgame.jodd.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.val;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JWTUtils {
    public static DecodedJWT validateJWT(String token) {
        val secret = Algorithm.HMAC256("secret");
        val verifier = com.auth0.jwt.JWT.require(secret)
                .withIssuer("jodd")
                .build();
        return verifier.verify(token);
    }

    public static String createJWT(String id, String nickname) {
        val secret = Algorithm.HMAC256("secret");
        val expire = Instant.now().plus(1, ChronoUnit.DAYS);
        val expireDate = Date.from(expire);
        return JWT.create()
                .withClaim("userid", id)
                .withClaim("nickname", nickname)
                .withIssuer("jodd")
                .withExpiresAt(expireDate)
                .sign(secret);
    }
}
