package com.paypic.Payments.service.transaction;

import com.paypic.Payments.dto.transaction.TransactionRequestDTO;
import com.paypic.Payments.dto.transaction.TransactionResponseDTO;
import com.paypic.Payments.entities.client.TipoCliente;
import com.paypic.Payments.entities.client.User;
import com.paypic.Payments.entities.transaction.Transaction;
import com.paypic.Payments.mapper.transaction.TransactionRequestMapper;
import com.paypic.Payments.mapper.transaction.TransactionResponseMapper;
import com.paypic.Payments.mapper.user.UserResponseMapper;
import com.paypic.Payments.repositories.TransactionRepository;
import com.paypic.Payments.service.user.UserService;
import com.paypic.Payments.service.auth.AuthorizationService;
import com.paypic.Payments.service.notification.NotificationService;
import com.paypic.Payments.utils.exceptions.ClienteSemAutorizacaoParaTransferir;
import com.paypic.Payments.utils.exceptions.SaldoInsuficienteException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionResponseMapper transactionResponseMapper;
    private final TransactionRequestMapper transactionRequestMapper;
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;
    private final UserResponseMapper userResponseMapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionRequestMapper transactionRequestMapper, TransactionResponseMapper transactionResponseMapper, UserService userService, AuthorizationService authorizationService, NotificationService notificationService, UserResponseMapper userResponseMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionRequestMapper = transactionRequestMapper;
        this.transactionResponseMapper = transactionResponseMapper;
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
        this.userResponseMapper = userResponseMapper;
    }

    @Transactional
    public TransactionResponseDTO realizarTransferencia(TransactionRequestDTO dto) {
        User sender = userService.buscarClienteEntity(dto.getSender().getId());
        User receiver = userService.buscarClienteEntity(dto.getReceiver().getId());
        BigDecimal amount = dto.getAmount();

        validarTransferencia(sender, amount);

        sender.debitar(amount);
        receiver.creditar(amount);

        Transaction transaction = criarTransacao(dto, sender, receiver, amount);
        transactionRepository.save(transaction);

        userService.salvarCliente(sender);
        userService.salvarCliente(receiver);

        notificationService.sendNotification(userResponseMapper.map(receiver), "Transação Recebida no valor de R$ " + amount);

        return transactionResponseMapper.map(transaction);
    }

    private void temSaldoParaTransferir(User userComSaldo, BigDecimal valor){
        if (userComSaldo.getSaldo().compareTo(valor) < 0 ){
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência");
        }
    }

    private void temAutorizacaoParaTransferir(User userAutorizado){
        if (userAutorizado.getTipoCliente() == TipoCliente.LOJISTA){
            throw  new ClienteSemAutorizacaoParaTransferir("Lojistas não podem realizar transferências");
        }
    }

    private void validarTransferencia(User sender, BigDecimal valor){
        temAutorizacaoParaTransferir(sender);
        temSaldoParaTransferir(sender, valor);
        if (!authorizationService.isAuthorized()){
            throw new ClienteSemAutorizacaoParaTransferir("Cliente não autorizado para realizar a transferência");
        }
    }

    private Transaction criarTransacao(TransactionRequestDTO dto, User sender, User receiver, BigDecimal amount){
        Transaction transaction = transactionRequestMapper.map(dto);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }

}
