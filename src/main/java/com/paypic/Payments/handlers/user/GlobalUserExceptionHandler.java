package com.paypic.Payments.handlers.user;

import com.paypic.Payments.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalUserExceptionHandler {

    @ExceptionHandler(UsuarioJaExisteException.class)
    public ResponseEntity<Object> handleUsuarioJaExiste(UsuarioJaExisteException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<Object> handleClienteNaoEncontrado(ClienteNaoEncontradoException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ClienteSemAutorizacaoParaTransferir.class)
    public ResponseEntity<Object> handleSemAutorizacao(ClienteSemAutorizacaoParaTransferir ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Object> hadleClienteSemSaldo(SaldoInsuficienteException ex){
        return buildResponse(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }

    @ExceptionHandler(TransacaoNaoEncontrada.class)
    public ResponseEntity<Object> handleTransacaoNaoEncontrada(TransacaoNaoEncontrada ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DocumentoInvalidoException.class)
    public ResponseEntity<Object> handleDocumentoInvalido(DocumentoInvalidoException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "message", message
        );
        return ResponseEntity.status(status).body(body);
    }
}
