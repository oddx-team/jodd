package io.oddgame.jodd.modules.authenticate;

public interface AuthenticateService {
    String signUp(String username, String password, String email);

    String login(String username, String password) throws Exception;
}
