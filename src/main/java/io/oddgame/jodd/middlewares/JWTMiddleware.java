package io.oddgame.jodd.middlewares;

import com.jinyframework.core.AbstractRequestBinder.Context;
import com.jinyframework.core.AbstractRequestBinder.HttpResponse;
import io.oddgame.jodd.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class JWTMiddleware {
    public static HttpResponse jwtMiddleware(Context ctx) {
        try {
            val token = ctx.headerParam("cookie").replace("token=", "");
            val decoded = JWTUtils.validateJWT(token);
            val userId = decoded.getClaim("userid");
            val nickname = decoded.getClaim("nickname");
            ctx.setDataParam("userid", userId.asString());
            ctx.setDataParam("nickname", nickname.asString());
            return HttpResponse.next();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return HttpResponse.reject("Unauthorized").status(401);
        }
    }
}
