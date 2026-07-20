package com.paypic.Payments.controllers.user;

import com.paypic.Payments.dto.user.UserRequestDTO;
import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.mapper.user.UserRequestMapper;
import com.paypic.Payments.mapper.user.UserResponseMapper;
import com.paypic.Payments.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final UserResponseMapper userResponseMapper;

    public UserController(UserService userService, UserRequestMapper userRequestMapper, UserResponseMapper userResponseMapper) {
        this.userService = userService;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
    }

    @GetMapping("/health")
    public ResponseEntity<String> apiFuncionando(){
        return ResponseEntity.ok("API funcionando!");
    }

    @PostMapping("/criarCliente")
    public ResponseEntity<UserResponseDTO> criarCliente(@RequestBody UserRequestDTO user){
        UserResponseDTO userResponse = userService.criarNovoCliente(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/procurar/cliente/{id}")
    public ResponseEntity<Optional<UserResponseDTO>> procurarCliente(@PathVariable Long id) {
        Optional<UserResponseDTO> userEncontrado = userService.procurarCliente(id);
        return ResponseEntity.ok(userEncontrado);
    }
}
