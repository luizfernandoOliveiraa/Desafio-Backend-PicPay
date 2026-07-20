package com.paypic.Payments.utils.exceptions;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(String message) {
        super(message);
    }

    public ClienteNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
