package com.paypic.Payments.entities.client;

import com.paypic.Payments.dto.user.UserResponseDTO;
import com.paypic.Payments.utils.exceptions.SaldoInsuficienteException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "users")
@Table(name = "tb_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String documentoIdentificacao; //Cpf para PF e CNPJ para PJ

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente; // Comum, Lojista

    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa; // PF ou PJ

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String senha;

    private BigDecimal saldo;

    public void debitar(BigDecimal valor){
        if (this.saldo.compareTo(valor) < 0){
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public void creditar(BigDecimal valor){
        this.saldo = this.saldo.add(valor);
    }
}
