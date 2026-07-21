package com.paypic.Payments.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypic.Payments.dto.user.UserRequestDTO;
import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.entities.client.TipoCliente;
import com.paypic.Payments.entities.client.TipoPessoa;
import com.paypic.Payments.handlers.user.GlobalUserExceptionHandler;
import com.paypic.Payments.service.user.UserService;
import com.paypic.Payments.exceptions.UsuarioJaExisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setup() {
        // Configura o MockMvc para testar o UserController de forma isolada
        // Adiciona o ExceptionHandler REAL da aplicação para que o teste reflita o comportamento de produção
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalUserExceptionHandler())
                .build();

        // Inicializa os DTOs para serem usados nos testes
        userRequestDTO = new UserRequestDTO(
                "John", "Doe", "john.doe@example.com",new BigDecimal("100.00"), "12345678901", TipoCliente.COMUM, TipoPessoa.PF,
                "password");

        userResponseDTO = new UserResponseDTO(
                1L, "John", "Doe", "john.doe@example.com", new BigDecimal("100.00"), TipoCliente.COMUM, TipoPessoa.PF
        );
    }

    @Test
    void healthCheckShouldReturnOk() throws Exception {
        mockMvc.perform(get("/users/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("API funcionando!"));
    }

    @Test
    void criarClienteQueDeveRetornarCreated() throws Exception {
        // Configura o mock do serviço para retornar o DTO de resposta quando chamado
        when(userService.criarNovoCliente(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // Executa a requisição POST e verifica os resultados
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userResponseDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(userResponseDTO.getFirstName()));
    }

    @Test
    void criarClienteQueDeveRetornarConflitoDeCriacao() throws Exception {
        // Configura o mock do serviço para lançar uma exceção
        when(userService.criarNovoCliente(any(UserRequestDTO.class)))
                .thenThrow(new UsuarioJaExisteException("Usuário com este email já existe."));

        // Executa a requisição POST e verifica se o status 409 (Conflict) e a mensagem de erro no JSON são retornados
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Usuário com este email já existe."));
    }
}