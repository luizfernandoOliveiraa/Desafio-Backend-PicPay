package com.paypic.Payments.exceptions;

public class ClienteSemAutorizacaoParaTransferirException extends RuntimeException {

    public ClienteSemAutorizacaoParaTransferirException(String message) {
        super(message);
    }

    public ClienteSemAutorizacaoParaTransferirException(String message, Throwable cause) {
        super(message, cause);
    }

}
