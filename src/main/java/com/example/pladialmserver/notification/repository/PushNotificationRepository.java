package com.example.pladialmserver.notification.repository;

import com.example.pladialmserver.notification.entity.PushNotification;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long> {
    Page<PushNotification> findByUserAndIsEnableOrderByCreatedAtDesc(User user, Boolean isEnable, Pageable pageable);
}
