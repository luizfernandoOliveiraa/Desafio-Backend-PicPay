package com.paypic.Payments.entities.client.valueObjects;

public sealed interface DocumentoIdentificacao permits Cpf, Cnpj{
    String getValor();
}
