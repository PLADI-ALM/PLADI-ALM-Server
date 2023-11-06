package com.example.pladialmserver.global.feign.publisher;

import com.example.pladialmserver.user.entity.User;

public interface ArchivingServerEventPublisher {
    void addUser(User user);

    void changeUserProfile(User user);

    void deleteUser(User user);
}
