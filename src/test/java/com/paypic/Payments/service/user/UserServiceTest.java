package com.paypic.Payments.service.user;

import com.paypic.Payments.dto.user.UserRequestDTO;
import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.entities.client.TipoCliente;
import com.paypic.Payments.entities.client.TipoPessoa;
import com.paypic.Payments.entities.client.User;
import com.paypic.Payments.mapper.user.UserRequestMapper;
import com.paypic.Payments.mapper.user.UserResponseMapper;
import com.paypic.Payments.repositories.UserRepository;
import com.paypic.Payments.exceptions.ClienteNaoEncontradoException;
import com.paypic.Payments.exceptions.UsuarioJaExisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setDocumentoIdentificacao("12345678901");
        user.setTipoCliente(TipoCliente.COMUM);
        user.setSaldo(new BigDecimal("100.00"));

        userRequestDTO = new UserRequestDTO(
                "John", "Doe", "john.doe@example.com", new BigDecimal("100.00"),"12345678901", TipoCliente.COMUM, TipoPessoa.PF,
                  "password");

        userResponseDTO = new UserResponseDTO(
                1L, "John", "Doe", "john.doe@example.com", new BigDecimal("100.00"),TipoCliente.COMUM ,TipoPessoa.PF );
    }

    @Test
    void criarNovoCliente_Success() {
        when(userRepository.findUserByDocumentoIdentificacao(anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRequestMapper.map(any(UserRequestDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userResponseMapper.map(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.criarNovoCliente(userRequestDTO);

        assertNotNull(result);
        assertEquals(userResponseDTO, result);
    }

    @Test
    void criarNovoCliente_DocumentoJaExiste() {
        when(userRepository.findUserByDocumentoIdentificacao(anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(UsuarioJaExisteException.class, () -> {
            userService.criarNovoCliente(userRequestDTO);
        });

        assertEquals("Usuário com este documento de identificação já existe.", exception.getMessage());
    }

    @Test
    void criarNovoCliente_EmailJaExiste() {
        when(userRepository.findUserByDocumentoIdentificacao(anyString())).thenReturn(Optional.empty());
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(UsuarioJaExisteException.class, () -> {
            userService.criarNovoCliente(userRequestDTO);
        });

        assertEquals("Usuário com este email já existe.", exception.getMessage());
    }

    @Test
    void procurarCliente_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userResponseMapper.map(user)).thenReturn(userResponseDTO);

        Optional<UserResponseDTO> result = userService.procurarCliente(1L);

        assertTrue(result.isPresent());
        assertEquals(userResponseDTO, result.get());
    }

    @Test
    void procurarCliente_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ClienteNaoEncontradoException.class, () -> {
            userService.procurarCliente(1L);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void buscarClienteEntity_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.buscarClienteEntity(1L);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void buscarClienteEntity_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ClienteNaoEncontradoException.class, () -> {
            userService.buscarClienteEntity(1L);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void salvarCliente() {
        userService.salvarCliente(user);
        verify(userRepository, times(1)).save(user);
    }
}