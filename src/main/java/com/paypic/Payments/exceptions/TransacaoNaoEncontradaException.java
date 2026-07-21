package com.paypic.Payments.exceptions;

public class TransacaoNaoEncontradaException extends RuntimeException {
    public TransacaoNaoEncontradaException(String message) {
        super(message);
    }

    public TransacaoNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }

}
