package com.rio.MessengerService.Repository;

import com.rio.MessengerService.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findTokenByUsername(String username);
}
