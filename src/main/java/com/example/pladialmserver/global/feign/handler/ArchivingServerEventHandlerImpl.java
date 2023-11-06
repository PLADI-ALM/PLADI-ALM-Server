package com.example.pladialmserver.global.feign.handler;

import com.example.pladialmserver.global.feign.event.DeleteUserEvent;
import com.example.pladialmserver.global.feign.event.UserEvent;
import com.example.pladialmserver.global.feign.feignClient.ArchivingServerClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ArchivingServerEventHandlerImpl implements ArchivingServerEventHandler {
    private final ArchivingServerClient archivingServerClient;

    @Async
    @EventListener
    @Override
    public void addUser(UserEvent userEvent) {
        try {
            archivingServerClient.addUser(userEvent.toDto());
        } catch (FeignException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    @EventListener
    @Override
    public void changeUserProfile(UserEvent userEvent) {
        try {
            archivingServerClient.changeUser(userEvent.toDto());
        } catch (FeignException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    @EventListener
    @Override
    public void deleteUser(DeleteUserEvent userEvent) {
        archivingServerClient.deleteUser(userEvent.toDto());
    }

}
