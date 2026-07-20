package com.paypic.Payments.service.user;

import com.paypic.Payments.dto.user.UserRequestDTO;
import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.entities.client.TipoCliente;
import com.paypic.Payments.entities.client.User;
import com.paypic.Payments.mapper.user.UserRequestMapper;
import com.paypic.Payments.mapper.user.UserResponseMapper;
import com.paypic.Payments.repositories.UserRepository;
import com.paypic.Payments.utils.exceptions.ClienteNaoEncontradoException;
import com.paypic.Payments.utils.exceptions.ClienteSemAutorizacaoParaTransferir;
import com.paypic.Payments.utils.exceptions.UsuarioJaExisteException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    public UserService(UserRepository userRepository, UserRequestMapper userRequestMapper, UserResponseMapper userResponseMapper) {
        this.userRepository = userRepository;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
    }

    public UserResponseDTO criarNovoCliente(UserRequestDTO userRequestDTO){
        if (userRepository.findUserByDocumentoIdentificacao(userRequestDTO.getDocumentoIdentificacao()).isPresent()) {
            throw new UsuarioJaExisteException("Usuário com este documento de identificação já existe.");
        }
        if (userRepository.findUserByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new UsuarioJaExisteException("Usuário com este email já existe.");
        }
        User userModel = userRequestMapper.map(userRequestDTO);
        User salvarUser = userRepository.save(userModel);
        return userResponseMapper.map(salvarUser);
    }

    public Optional<UserResponseDTO> procurarCliente(Long id) {
        return Optional.ofNullable(userResponseMapper.map(buscarClienteEntity(id)));
    }

    public User buscarClienteEntity(Long id){
        return userRepository.findById(id).
                orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
    }

    public void salvarCliente(User user) {
        userRepository.save(user);
    }
}