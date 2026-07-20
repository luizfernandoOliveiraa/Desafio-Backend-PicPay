package com.paypic.Payments.mapper.transaction;

import com.paypic.Payments.dto.transaction.TransactionResponseDTO;
import com.paypic.Payments.entities.transaction.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionResponseMapper {

    public TransactionResponseDTO map(Transaction transaction){
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setId(transaction.getId());
        transactionResponseDTO.setAmount(transaction.getAmount());
        transactionResponseDTO.setReceiver(transaction.getReceiver());
        transactionResponseDTO.setTimestamp(transaction.getTimestamp());
        return transactionResponseDTO;
    }
}
