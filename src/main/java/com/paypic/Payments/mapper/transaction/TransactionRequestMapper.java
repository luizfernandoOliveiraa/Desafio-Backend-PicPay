package com.paypic.Payments.mapper.transaction;

import com.paypic.Payments.dto.transaction.TransactionRequestDTO;
import com.paypic.Payments.entities.transaction.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionRequestMapper {

    public Transaction map(TransactionRequestDTO transactionRequestDTO){
        Transaction transactionModel = new Transaction();
        transactionModel.setAmount(transactionRequestDTO.getAmount());
        transactionModel.setSender(transactionRequestDTO.getSender());
        transactionModel.setReceiver(transactionRequestDTO.getReceiver());
        return transactionModel;
    }
}
