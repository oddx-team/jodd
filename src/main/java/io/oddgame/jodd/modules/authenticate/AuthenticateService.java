package io.oddgame.jodd.modules.authenticate;

public interface AuthenticateService {
    User signUp(String username, String password, String email);

    String login(String username, String password) throws Exception;
}
