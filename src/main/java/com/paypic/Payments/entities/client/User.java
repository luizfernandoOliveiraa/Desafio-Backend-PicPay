package com.paypic.Payments.entities.client;

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
}
