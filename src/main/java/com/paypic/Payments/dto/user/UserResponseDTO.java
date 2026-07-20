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
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal saldo;
    private TipoCliente tipoCliente;
    private TipoPessoa tipoPessoa;

}
