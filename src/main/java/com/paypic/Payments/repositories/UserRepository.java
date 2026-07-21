package com.paypic.Payments.repositories;

import com.paypic.Payments.entities.client.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByDocumentoIdentificacao(String documentoIdentificacao);

    Optional<User> findUserByEmail(String email);
}
