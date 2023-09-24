package com.example.pladialmserver.user.repository;

import com.example.pladialmserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
