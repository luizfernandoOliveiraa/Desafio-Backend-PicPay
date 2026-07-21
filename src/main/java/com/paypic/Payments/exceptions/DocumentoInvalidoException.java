package com.paypic.Payments.exceptions;

public class DocumentoInvalidoException extends RuntimeException {
    public DocumentoInvalidoException(String message) {
        super(message);
    }

    public DocumentoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
