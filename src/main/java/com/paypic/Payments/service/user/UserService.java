package com.paypic.Payments.service.user;

import com.paypic.Payments.dto.user.UserRequestDTO;
import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.entities.client.User;
import com.paypic.Payments.mapper.user.UserRequestMapper;
import com.paypic.Payments.mapper.user.UserResponseMapper;
import com.paypic.Payments.repositories.UserRepository;
import com.paypic.Payments.exceptions.ClienteNaoEncontradoException;
import com.paypic.Payments.exceptions.UsuarioJaExisteException;
import com.paypic.Payments.service.notification.NotificationService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository, UserRequestMapper userRequestMapper, UserResponseMapper userResponseMapper, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
        this.notificationService = notificationService;
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

    @Cacheable(value = "users", key = "#id")
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

    public List<UserResponseDTO> buscarTodosClientes(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userResponseMapper::map)
                .toList();
    }

    @CacheEvict(value = "users", key = "#id")
    public void deletarCliente(Long id){
        User user = buscarClienteEntity(id);
        if (user != null ){
            userRepository.deleteById(id);
        } else {
            throw new ClienteNaoEncontradoException("Cliente não encontrado");
        }
    }

    @CachePut(value = "users", key = "#id")
    public Optional<UserResponseDTO> atualizarCliente(Long id, UserRequestDTO userRequestDTO){
        Optional<User> clienteExistente = userRepository.findById(id);
        if (clienteExistente.isPresent()){
            User clienteAtualizado = userRequestMapper.map(userRequestDTO);
            clienteAtualizado.setId(id);
            User clienteSalvo = userRepository.save(clienteAtualizado);
            return Optional.of(userResponseMapper.map(clienteSalvo));
        } else {
            return Optional.empty();
        }
    }

}