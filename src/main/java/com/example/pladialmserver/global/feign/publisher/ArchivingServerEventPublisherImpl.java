package com.example.pladialmserver.global.feign.publisher;

import com.example.pladialmserver.global.feign.event.UserEvent;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArchivingServerEventPublisherImpl implements ArchivingServerEventPublisher{
    private final ApplicationEventPublisher publisher;
    @Override
    public void addUser(User user) {
        publisher.publishEvent(UserEvent.toEvent(user));
    }

    @Override
    public void changeUserProfile(User user) {
        publisher.publishEvent(UserEvent.toEvent(user));
    }

    @Override
    public void deleteUser(User user) {
        publisher.publishEvent(UserEvent.toEvent(user));
    }
}
