package com.paypic.Payments.mapper.user;

import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.entities.client.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    public UserResponseDTO map(User userModel){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userModel.getId());
        userResponseDTO.setFirstName(userModel.getFirstName());
        userResponseDTO.setLastName(userModel.getLastName());
        userResponseDTO.setEmail(userModel.getEmail());
        userResponseDTO.setSaldo(userModel.getSaldo());
        userResponseDTO.setTipoCliente(userModel.getTipoCliente());
        userResponseDTO.setTipoPessoa(userModel.getTipoPessoa());
        return userResponseDTO;
    }
}
