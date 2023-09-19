package com.example.pladialmserver.global.user.repository;

import com.example.pladialmserver.global.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdAndIsEnable(Long userId, Boolean isEnable);
}
