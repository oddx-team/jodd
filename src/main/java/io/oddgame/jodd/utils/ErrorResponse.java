package io.oddgame.jodd.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private final String message;
    private final StackTraceElement[] stackTrace;
}
