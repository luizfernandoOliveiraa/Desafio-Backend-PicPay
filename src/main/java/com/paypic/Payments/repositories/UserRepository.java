package com.paypic.Payments.repositories;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.paypic.Payments.entities.client.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByDocument(String document);

}
