package com.example.pladialmserver.user.repository.user;

import com.example.pladialmserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustom{
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
