package com.paypic.Payments.dto.transaction;

import com.paypic.Payments.entities.client.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private Long id;
    private BigDecimal amount;
    private User receiver;
    private LocalDateTime timestamp;
}
