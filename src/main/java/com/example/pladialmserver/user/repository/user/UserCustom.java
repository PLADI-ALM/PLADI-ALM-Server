package com.example.pladialmserver.user.repository.user;

import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCustom {
    Page<User> findAllByName(String name, Pageable pageable);
    List<User> findAllByName(String name);
}