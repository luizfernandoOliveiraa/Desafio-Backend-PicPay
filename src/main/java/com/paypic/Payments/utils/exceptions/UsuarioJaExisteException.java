package com.paypic.Payments.utils.exceptions;

public class UsuarioJaExisteException extends RuntimeException {
    public UsuarioJaExisteException(String message) {
        super(message);
    }

    public UsuarioJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
