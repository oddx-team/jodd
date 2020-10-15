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

    public static HttpResponse login(Context ctx) throws Exception {
        val loginCreds = gson.fromJson(ctx.getBody(), LoginRequest.class);
        val token = authenticateService.login(loginCreds.username, loginCreds.password);
        ctx.putHeader("Set-Cookie", "token=" + token + "; Max-Age=86400; Path=/; HttpOnly");
        return HttpResponse.of(new LoginResponse(token));
    }

    public static HttpResponse signUp(Context ctx) {
        val signUpCreds = gson.fromJson(ctx.getBody(), User.class);
        val insertedId = authenticateService.signUp(signUpCreds.getUsername(), signUpCreds.getPassword(), signUpCreds.getEmail());
        return HttpResponse.of("Registered ID: " + insertedId);
    }

    @AllArgsConstructor
    private static class LoginRequest {
        private final String username;
        private final String password;
    }

    @AllArgsConstructor
    private static class LoginResponse {
        private final String token;
    }
}
