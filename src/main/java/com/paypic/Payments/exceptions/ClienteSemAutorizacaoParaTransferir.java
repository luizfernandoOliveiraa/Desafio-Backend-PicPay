package com.paypic.Payments.exceptions;

public class ClienteSemAutorizacaoParaTransferir extends RuntimeException{

    public ClienteSemAutorizacaoParaTransferir(String message) {
        super(message);
    }

    public ClienteSemAutorizacaoParaTransferir(String message, Throwable cause) {
        super(message, cause);
    }

}
