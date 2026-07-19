package com.paypic.Payments.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long Id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal saldo;
    private String documentoIdentificacao;

}
