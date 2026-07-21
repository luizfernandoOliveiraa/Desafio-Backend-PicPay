package com.paypic.Payments.dto.transaction;

import com.paypic.Payments.entities.client.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {

    private BigDecimal amount;
    private Long senderId;
    private Long receiverId;

}
