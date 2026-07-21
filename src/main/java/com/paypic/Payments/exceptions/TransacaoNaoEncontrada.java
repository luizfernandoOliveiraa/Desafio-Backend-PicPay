package com.paypic.Payments.exceptions;

public class TransacaoNaoEncontrada extends RuntimeException {
    public TransacaoNaoEncontrada(String message) {
        super(message);
    }

    public TransacaoNaoEncontrada(String message, Throwable cause) {
        super(message, cause);
    }

}
