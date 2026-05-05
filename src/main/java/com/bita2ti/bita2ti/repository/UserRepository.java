package com.bita2ti.bita2ti.repository;

import com.bita2ti.bita2ti.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByDigitalId(String digitalId);
}