package io.oddgame.jodd.middlewares;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jinyframework.core.AbstractRequestBinder.Context;
import com.jinyframework.core.AbstractRequestBinder.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class JWTMiddleware {
    public static HttpResponse jwtMiddleware(Context ctx) {
        try {
            val token = ctx.headerParam("cookie").replace("token=", "");
            val secret = Algorithm.HMAC256("secret");
            val verifier = JWT.require(secret)
                    .withIssuer("jodd")
                    .build();
            val decoded = verifier.verify(token);
            val username = decoded.getClaim("username");
            ctx.setDataParam("username", username.asString());
            return HttpResponse.next();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return HttpResponse.reject("Unauthorized").status(401);
        }
    }
}
