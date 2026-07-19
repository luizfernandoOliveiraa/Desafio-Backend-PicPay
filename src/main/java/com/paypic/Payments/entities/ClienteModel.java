package com.paypic.Payments.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "tb_clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(unique = true)
    private String documentoIdentificacao; //Cpf para PF e CNPJ para PJ

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente; // Comum, Lojista

    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa; // PF ou PJ

    private String nomeCompleto;

    @Column(unique = true)
    private String email;

    private String senha;

}
