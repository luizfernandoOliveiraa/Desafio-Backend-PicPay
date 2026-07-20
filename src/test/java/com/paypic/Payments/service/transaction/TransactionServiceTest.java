package com.paypic.Payments.service.transaction;

import com.paypic.Payments.dto.transaction.TransactionRequestDTO;
import com.paypic.Payments.dto.transaction.TransactionResponseDTO;
import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.entities.client.TipoCliente;
import com.paypic.Payments.entities.client.TipoPessoa;
import com.paypic.Payments.entities.client.User;
import com.paypic.Payments.entities.transaction.Transaction;
import com.paypic.Payments.mapper.transaction.TransactionRequestMapper;
import com.paypic.Payments.mapper.transaction.TransactionResponseMapper;
import com.paypic.Payments.mapper.user.UserResponseMapper;
import com.paypic.Payments.repositories.TransactionRepository;
import com.paypic.Payments.service.auth.AuthorizationService;
import com.paypic.Payments.service.notification.NotificationService;
import com.paypic.Payments.service.user.UserService;
import com.paypic.Payments.utils.exceptions.ClienteSemAutorizacaoParaTransferir;
import com.paypic.Payments.utils.exceptions.SaldoInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionResponseMapper transactionResponseMapper;

    @Mock
    private TransactionRequestMapper transactionRequestMapper;

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;
    private TransactionRequestDTO transactionRequestDTO;
    private Transaction transaction;
    private TransactionResponseDTO transactionResponseDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);
        sender.setTipoCliente(TipoCliente.COMUM);
        sender.setSaldo(new BigDecimal("100.00"));

        receiver = new User();
        receiver.setId(2L);
        receiver.setSaldo(new BigDecimal("50.00"));

        transactionRequestDTO = new TransactionRequestDTO(new BigDecimal("25.00"), sender, receiver);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(new BigDecimal("25.00"));
        transaction.setTimestamp(LocalDateTime.now());

        transactionResponseDTO = new TransactionResponseDTO(1L, new BigDecimal("25.00"), receiver, transaction.getTimestamp());
        userResponseDTO = new UserResponseDTO(2L, "Jane", "Doe", "jane.doe@example.com", new BigDecimal("75.00"), TipoCliente.COMUM, TipoPessoa.PF);
    }

    @Test
    void realizarTransferencia_Success() {
        // ARRANGE
        when(userService.buscarClienteEntity(1L)).thenReturn(sender);
        when(userService.buscarClienteEntity(2L)).thenReturn(receiver);
        when(authorizationService.isAuthorized()).thenReturn(true);
        when(transactionRequestMapper.map(any(TransactionRequestDTO.class))).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionResponseMapper.map(any(Transaction.class))).thenReturn(transactionResponseDTO);
        when(userResponseMapper.map(receiver)).thenReturn(userResponseDTO);

        // ACT
        TransactionResponseDTO result = transactionService.realizarTransferencia(transactionRequestDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals(transactionResponseDTO, result);

        // Usa ArgumentCaptor para capturar os usuários passados para o método de salvar
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(2)).salvarCliente(userCaptor.capture());
        List<User> capturedUsers = userCaptor.getAllValues();

        // Verifica os saldos dos usuários capturados
        User capturedSender = capturedUsers.stream().filter(u -> u.getId().equals(sender.getId())).findFirst().orElseThrow();
        User capturedReceiver = capturedUsers.stream().filter(u -> u.getId().equals(receiver.getId())).findFirst().orElseThrow();

        assertEquals(new BigDecimal("75.00"), capturedSender.getSaldo());
        assertEquals(new BigDecimal("75.00"), capturedReceiver.getSaldo());

        verify(notificationService, times(1)).sendNotification(any(UserResponseDTO.class), anyString());
    }

    @Test
    void realizarTransferencia_SaldoInsuficiente() {
        sender.setSaldo(new BigDecimal("20.00"));
        when(userService.buscarClienteEntity(1L)).thenReturn(sender);
        when(userService.buscarClienteEntity(2L)).thenReturn(receiver);

        Exception exception = assertThrows(SaldoInsuficienteException.class, () -> {
            transactionService.realizarTransferencia(transactionRequestDTO);
        });

        assertEquals("Saldo insuficiente para realizar a transferência", exception.getMessage());
    }

    @Test
    void realizarTransferencia_LojistaNaoPodeTransferir() {
        sender.setTipoCliente(TipoCliente.LOJISTA);
        when(userService.buscarClienteEntity(1L)).thenReturn(sender);
        when(userService.buscarClienteEntity(2L)).thenReturn(receiver);

        Exception exception = assertThrows(ClienteSemAutorizacaoParaTransferir.class, () -> {
            transactionService.realizarTransferencia(transactionRequestDTO);
        });

        assertEquals("Lojistas não podem realizar transferências", exception.getMessage());
    }

    @Test
    void realizarTransferencia_NaoAutorizado() {
        when(userService.buscarClienteEntity(1L)).thenReturn(sender);
        when(userService.buscarClienteEntity(2L)).thenReturn(receiver);
        when(authorizationService.isAuthorized()).thenReturn(false);

        Exception exception = assertThrows(ClienteSemAutorizacaoParaTransferir.class, () -> {
            transactionService.realizarTransferencia(transactionRequestDTO);
        });

        assertEquals("Cliente não autorizado para realizar a transferência", exception.getMessage());
    }
}