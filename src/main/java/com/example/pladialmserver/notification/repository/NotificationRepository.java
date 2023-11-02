package com.example.pladialmserver.notification.repository;

import com.example.pladialmserver.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
