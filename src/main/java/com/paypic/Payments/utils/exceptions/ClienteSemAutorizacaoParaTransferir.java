package com.paypic.Payments.utils.exceptions;

public class ClienteSemAutorizacaoParaTransferir extends RuntimeException{

    public ClienteSemAutorizacaoParaTransferir(String message) {
        super(message);
    }

    public ClienteSemAutorizacaoParaTransferir(String message, Throwable cause) {
        super(message, cause);
    }

}
