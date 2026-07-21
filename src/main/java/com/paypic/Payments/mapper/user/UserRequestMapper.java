package com.paypic.Payments.mapper.user;

import com.paypic.Payments.dto.user.UserRequestDTO;
import com.paypic.Payments.entities.client.TipoPessoa;
import com.paypic.Payments.entities.client.User;
import com.paypic.Payments.entities.client.valueObjects.Cnpj;
import com.paypic.Payments.entities.client.valueObjects.Cpf;
import com.paypic.Payments.entities.client.valueObjects.DocumentoIdentificacao;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {

    public User map(UserRequestDTO userRequestDTO){
        User userModel = new User();
        userModel.setEmail(userRequestDTO.getEmail());
        userModel.setSaldo(userRequestDTO.getSaldo());
        userModel.setSenha(userRequestDTO.getSenha());
        userModel.setFirstName(userRequestDTO.getFirstName());
        userModel.setLastName(userRequestDTO.getLastName());
        userModel.setTipoCliente(userRequestDTO.getTipoCliente());
        userModel.setTipoPessoa(userRequestDTO.getTipoPessoa());

        DocumentoIdentificacao documento = userRequestDTO.getTipoPessoa() == TipoPessoa.PF
                ? Cpf.of(userRequestDTO.getDocumentoIdentificacao())
                : Cnpj.of(userRequestDTO.getDocumentoIdentificacao());
        userModel.setDocumentoIdentificacao(String.valueOf(documento));
        return userModel;
    }

}