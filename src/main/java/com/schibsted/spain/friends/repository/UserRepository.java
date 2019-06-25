package com.schibsted.spain.friends.repository;

import com.schibsted.spain.friends.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
