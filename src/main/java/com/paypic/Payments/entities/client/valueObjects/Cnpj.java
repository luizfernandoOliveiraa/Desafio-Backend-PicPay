package com.paypic.Payments.entities.client.valueObjects;

import java.util.Objects;

public final class Cnpj implements DocumentoIdentificacao {

    private final String valor;

    private Cnpj(String valor){
        this.valor = valor;
    }
    public static Cnpj of(String valor) {
        String digits = valor == null ? "" : valor.replaceAll("\\D", "");
        if (!isValido(digits)) {
            throw new IllegalArgumentException("CNPJ inválido: " + valor);
        }
        return new Cnpj(digits);
    }

    private static boolean isValido(String cnpj) {
        if (cnpj.length() != 14 || cnpj.chars().distinct().count() == 1) {
            return false;
        }
        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return digitoVerificador(cnpj, pesos1, 12) == Character.getNumericValue(cnpj.charAt(12))
                && digitoVerificador(cnpj, pesos2, 13) == Character.getNumericValue(cnpj.charAt(13));
    }

    private static int digitoVerificador(String cnpj, int[] pesos, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    @Override
    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Cnpj other && valor.equals(other.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
