package io.oddgame.jodd.modules.authenticate;

import com.google.gson.Gson;
import com.jinyframework.core.AbstractRequestBinder.Context;
import com.jinyframework.core.AbstractRequestBinder.HttpResponse;
import io.oddgame.jodd.factories.AppFactory;
import io.oddgame.jodd.factories.ServiceFactory;
import lombok.AllArgsConstructor;
import lombok.val;

public class AuthenticateHandler {
    private static final Gson gson = AppFactory.getGson();
    private static final AuthenticateService authenticateService = ServiceFactory.getAuthenticateService();

    public static HttpResponse enterGame(Context ctx) throws Exception {
        val loginCreds = gson.fromJson(ctx.getBody(), LoginRequest.class);
        val token = authenticateService.enterGame(loginCreds.nickname, null);
        ctx.putHeader("Set-Cookie", "token=" + token + "; Max-Age=86400; Path=/; HttpOnly");
        return HttpResponse.of(new LoginResponse(token));
    }

    @AllArgsConstructor
    private static class LoginRequest {
        private final String nickname;
    }

    @AllArgsConstructor
    private static class LoginResponse {
        private final String token;
    }
}
