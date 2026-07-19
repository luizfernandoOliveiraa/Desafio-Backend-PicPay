package com.paypic.Payments.mapper.user;

import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.entities.client.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    public UserResponseDTO map(User userModel){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userModel.getId());
        userResponseDTO.setFirstName(userResponseDTO.getFirstName());
        userResponseDTO.setLastName(userResponseDTO.getLastName());
        userResponseDTO.setEmail(userModel.getEmail());
        userResponseDTO.setSaldo(userModel.getSaldo());
        userResponseDTO.setDocumentoIdentificacao(userModel.getDocumentoIdentificacao());
        return userResponseDTO;
    }
}
