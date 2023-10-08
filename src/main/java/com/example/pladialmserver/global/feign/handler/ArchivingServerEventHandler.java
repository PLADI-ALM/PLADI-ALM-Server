package com.example.pladialmserver.global.feign.handler;


import com.example.pladialmserver.global.feign.event.DeleteUserEvent;
import com.example.pladialmserver.global.feign.event.UpdateUserEvent;
import com.example.pladialmserver.global.feign.event.UserEvent;

public interface ArchivingServerEventHandler {

    void addUser(UserEvent userEvent);

    void changeUserProfile(UpdateUserEvent userEvent);

    void deleteUser(DeleteUserEvent userEvent);
}
