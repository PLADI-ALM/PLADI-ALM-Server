package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.notification.entity.PushNotification;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Data
public class NotificationRes {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    private LocalDateTime createdAt;
    private String notificationInfo;
    private String notificationType;


    @Builder
    public NotificationRes(LocalDateTime createdAt, String notificationInfo, String notificationType) {
        this.createdAt = createdAt;
        this.notificationInfo = notificationInfo;
        this.notificationType = notificationType;
    }

    public static NotificationRes toDto(PushNotification notification) {
        return NotificationRes.builder()
                .createdAt(notification.getCreatedAt())
                .notificationInfo(notification.getNotificationInfo())
                .notificationType(notification.getNotificationType())
                .build();
    }
}
