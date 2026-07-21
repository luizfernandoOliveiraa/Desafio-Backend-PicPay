package com.paypic.Payments.controllers.user;

import com.paypic.Payments.dto.user.UserRequestDTO;
import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.mapper.user.UserRequestMapper;
import com.paypic.Payments.mapper.user.UserResponseMapper;
import com.paypic.Payments.service.user.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173") // Adicionando para garantir
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> apiFuncionando(){
        return ResponseEntity.ok("API funcionando!");
    }

    @PostMapping("/criarCliente") // Corrigido para camelCase
    public ResponseEntity<UserResponseDTO> criarCliente(@RequestBody UserRequestDTO user){
        UserResponseDTO userResponse = userService.criarNovoCliente(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/procurar/cliente/{id}")
    public ResponseEntity<Optional<UserResponseDTO>> procurarCliente(@PathVariable Long id) {
        Optional<UserResponseDTO> userEncontrado = userService.procurarCliente(id);
        return ResponseEntity.ok(userEncontrado);
    }

    @GetMapping("/procurar/todosClientes") // Corrigido para camelCase
    public ResponseEntity<Iterable<UserResponseDTO>> procurarTodosClientes() {
        Iterable<UserResponseDTO> users = userService.buscarTodosClientes();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deletar/cliente/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id){
        userService.deletarCliente(id);
        return ResponseEntity.ok("Cliente deletado com sucesso");
    }

    @PutMapping("/atualizar/cliente/{id}")
    public ResponseEntity<?> atulizarCliente(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO){
        Optional<UserResponseDTO> userAtualizado = userService.atualizarCliente(id, userRequestDTO);
        return userAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}