package com.example.pladialmserver.global.user.repository;

import com.example.pladialmserver.global.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
