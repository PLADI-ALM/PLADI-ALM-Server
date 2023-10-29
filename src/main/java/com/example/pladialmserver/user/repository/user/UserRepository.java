package com.example.pladialmserver.user.repository.user;

import com.example.pladialmserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustom{
    Optional<User> findByUserIdAndIsEnable(Long userId, Boolean isEnable);
    Optional<User> findByEmailAndIsEnable(String email, Boolean isEnable);
    Boolean existsByEmailAndIsEnable(String email, Boolean isEnable);
    Boolean existsByPhoneAndIsEnable(String phone, Boolean isEnable);
    Boolean existsByPhoneAndUserIdNotAndIsEnable(String phone, Long userId, Boolean isEnable);
}
