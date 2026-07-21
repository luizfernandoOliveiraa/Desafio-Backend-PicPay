package com.paypic.Payments.controllers.transaction;

import com.paypic.Payments.dto.transaction.TransactionRequestDTO;
import com.paypic.Payments.dto.transaction.TransactionResponseDTO;
import com.paypic.Payments.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/health")
    @Operation(summary = "Check API status", description = "Verifica se a API está funcionando corretamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API funcionando!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> verificarSaudeApi(){
        return ResponseEntity.ok("API funcionando!");
    }

    @GetMapping("/listar_transacoes")
    @Operation(summary = "Listar todas as transações", description = "Retorna uma lista de todas as transações realizadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transações listadas com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<List<TransactionResponseDTO>> listarTransacoes() {
        List<TransactionResponseDTO> transactions = transactionService.listarTransacoes();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/listar_transacao/{id}")
    @Operation(summary = "Listar uma transação por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação encontrada!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TransactionResponseDTO> listarTransacaoPorID(
            @Parameter(description = "ID da transação", required = true)
            @PathVariable Long id) {
        TransactionResponseDTO transaction = transactionService.listarTransacaoPorID(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Cria a transaçaõ", description = "Endpoint utilizado para transferencias entre clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação realizada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TransactionResponseDTO> realizarTransferencia(
            @Parameter(description = "Body da transação contendo os dados da transação", required = true)
            @RequestBody TransactionRequestDTO dto) {
        TransactionResponseDTO transaction = transactionService.realizarTransferencia(dto);
        return ResponseEntity.ok(transaction);
    }
}
