package io.oddgame.jodd.modules.authenticate;

import io.oddgame.jodd.exceptions.LogicException;

public interface AuthenticateService {
    String signUp(String username, String password, String email);

    String login(String username, String password) throws LogicException;
}
