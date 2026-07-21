package com.paypic.Payments.exceptions;

public class UsuarioJaExisteException extends RuntimeException {
    public UsuarioJaExisteException(String message) {
        super(message);
    }

    public UsuarioJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
