package com.paypic.Payments.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {

    @NotNull(message = "Valor da transferência é obrigatório")
    @Positive
    private BigDecimal amount;
    @NotNull(message = "Id do remetente é obrigatório")
    private Long senderId;
    @NotNull(message = "Id do recebedor é obrigatório")
    private Long receiverId;

}
