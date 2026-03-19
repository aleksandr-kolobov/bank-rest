package com.example.bankcards.exception;

import java.text.MessageFormat;
import java.util.UUID;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(UUID token, String message) {
        super(MessageFormat.format("Error trying refresh by token: {0} : {1}", token, message));
    }
}
