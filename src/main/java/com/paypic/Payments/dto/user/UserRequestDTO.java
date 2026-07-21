package com.paypic.Payments.dto.user;

import com.paypic.Payments.entities.client.TipoCliente;
import com.paypic.Payments.entities.client.TipoPessoa;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "First name é obrigatório")
    private String firstName;

    @NotNull(message = "Last name é obrigatório")
    private String lastName;

    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "Saldo é obrigatório")
    @Positive(message = "Saldo deve ser positivo")
    private BigDecimal saldo;

    @NotNull(message = "Documento identificação é obrigatório")
    private String documentoIdentificacao;

    @NotNull(message = "Tipo cliente é obrigatório")
    private TipoCliente tipoCliente;

    @NotNull(message = "Tipo pessoa é obrigatório")
    private TipoPessoa tipoPessoa;

    @NotNull(message = "Senha é obrigatória")
    private String senha;

}
