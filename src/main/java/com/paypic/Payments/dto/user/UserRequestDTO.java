package com.paypic.Payments.dto.user;

import com.paypic.Payments.entities.client.TipoCliente;
import com.paypic.Payments.entities.client.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {


    private Long id;
    private String documentoIdentificacao;
    private TipoCliente tipoCliente;
    private TipoPessoa tipoPessoa;
    private String firstName;
    private String lastName;
    private String email;
    private String senha;
    private BigDecimal saldo;


}
