package io.oddgame.jodd.factories;

import io.oddgame.jodd.modules.authenticate.AuthenticateService;
import io.oddgame.jodd.modules.authenticate.AuthenticateServiceImpl;
import lombok.Setter;

@Setter
public class ServiceFactory {
    private static AuthenticateService authenticateService;

    public static AuthenticateService getAuthenticateService() {
        if (authenticateService == null) {
            authenticateService = new AuthenticateServiceImpl();
        }

        return authenticateService;
    }
}
