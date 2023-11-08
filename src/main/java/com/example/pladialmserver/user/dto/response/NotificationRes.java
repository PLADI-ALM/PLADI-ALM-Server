package com.example.pladialmserver.user.dto.response;

import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.notification.entity.PushNotification;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Data
public class NotificationRes {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_PATTERN)
    private LocalDateTime createdAt;
    @Schema(type = "String", description = "알림 내용", example = "맥북 예약이 승인되었습니다.")
    private String notificationInfo;
    @Schema(type = "String", description = "알림 제목", example = "장비 예약 승인")
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
