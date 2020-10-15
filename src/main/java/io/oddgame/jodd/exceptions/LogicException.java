package io.oddgame.jodd.exceptions;

public class LogicException extends Exception {
    private String message;

    public LogicException(String message) {
        super(message);
    }
}
