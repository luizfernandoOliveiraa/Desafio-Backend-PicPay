package com.paypic.Payments.controllers.user;

import com.paypic.Payments.dto.user.UserRequestDTO;
import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    @Operation(summary = "Check API status", description = "Verifica se a API está funcionando corretamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API funcionando!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<String> apiFuncionando() {
        return ResponseEntity.ok("API funcionando!");
    }

    @PostMapping("/criarCliente") // Corrigido para camelCase
    @Operation(summary = "Cria um novo cliente", description = "Endpoint utilizado para criar um novo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<UserResponseDTO> criarCliente(
            @Valid @RequestBody UserRequestDTO user) {
        UserResponseDTO userResponse = userService.criarNovoCliente(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/procurar/cliente/{id}")
    @Operation(summary = "Procura um cliente por ID", description = "Endpoint utilizado para procurar um cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Optional<UserResponseDTO>> procurarCliente(@PathVariable Long id) {
        Optional<UserResponseDTO> userEncontrado = userService.procurarCliente(id);
        return ResponseEntity.ok(userEncontrado);
    }

    @GetMapping("/procurar/todosClientes") // Corrigido para camelCase
    @Operation(summary = "Procura todos os clientes", description = "Endpoint utilizado para procurar todos os clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Iterable<UserResponseDTO>> procurarTodosClientes() {
        Iterable<UserResponseDTO> users = userService.buscarTodosClientes();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deletar/cliente/{id}")
    @Operation(summary = "Deleta um cliente por ID", description = "Endpoint utilizado para deletar um cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> deletarCliente(@PathVariable Long id) {
        userService.deletarCliente(id);
        return ResponseEntity.ok("Cliente deletado com sucesso");
    }

    @PutMapping("/atualizar/cliente/{id}")
    @Operation(summary = "Atualiza um cliente por ID", description = "Endpoint utilizado para atualizar um cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> atulizarCliente(@PathVariable Long id,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        Optional<UserResponseDTO> userAtualizado = userService.atualizarCliente(id, userRequestDTO);
        return userAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}