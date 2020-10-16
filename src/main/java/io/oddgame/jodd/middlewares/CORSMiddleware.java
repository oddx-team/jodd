package io.oddgame.jodd.middlewares;

import com.jinyframework.core.AbstractRequestBinder.Context;
import com.jinyframework.core.AbstractRequestBinder.HttpResponse;

public class CORSMiddleware {
    public static HttpResponse corsMiddleware(Context ctx) {
        ctx.putHeader("Access-Control-Allow-Origin", "*");
        return HttpResponse.next();
    }
}
