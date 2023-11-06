package com.example.pladialmserver.global.feign.handler;


import com.example.pladialmserver.global.feign.event.DeleteUserEvent;
import com.example.pladialmserver.global.feign.event.UserEvent;

public interface ArchivingServerEventHandler {

    void addUser(UserEvent userEvent);

    void changeUserProfile(UserEvent userEvent);

    void deleteUser(DeleteUserEvent userEvent);
}
