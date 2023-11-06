package com.example.pladialmserver.global.feign.publisher;

import com.example.pladialmserver.global.feign.event.DeleteUserEvent;
import com.example.pladialmserver.global.feign.event.UserEvent;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArchivingServerEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void addUser(User user) {
        publisher.publishEvent(UserEvent.toEvent(user));
    }

    public void changeUserProfile(User user) {
        publisher.publishEvent(UserEvent.toEvent(user));
    }

    public void deleteUser(User user) {
        publisher.publishEvent(DeleteUserEvent.toEvent(user));
    }
}
