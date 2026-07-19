package com.paypic.Payments.entities.client.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

public final class Cpf implements DocumentoIdentificacao {

    private final String valor;

    public Cpf(String valor) {
        this.valor = valor;
    }

    public static Cpf of(String valor) {
        String digits = valor == null ? "" : valor.replace("\\D", "");
        if (!isValido(digits)) {
            throw  new IllegalArgumentException("CPF inválido: " + valor);
        }
        return new Cpf(digits);
    }

    private static boolean isValido(String cpf){
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1 ){
            return false;
        }
        return digitoVerificador(cpf, 9) == Character.getNumericValue(cpf.charAt(9))
                && digitoVerificador(cpf, 10) == Character.getNumericValue(cpf.charAt(10));
    }

    private static int digitoVerificador(String cpf, int tamanho){
        int soma = 0;
        for (int i = 0; i < tamanho; i++){
            soma += Character.getNumericValue(cpf.charAt(i)) * ((tamanho + 1) - i);
        }
        int resto = (soma * 10 ) % 11;
        return resto == 10 ? 0 : resto;
    }

    @Override
    public String getValor(){
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Cpf other && valor.equals(other.valor);
    }

    @Override
    public int hashCode(){
        return Objects.hash(valor);
    }

    @Override
    public String toString(){
        return valor;
    }

}
