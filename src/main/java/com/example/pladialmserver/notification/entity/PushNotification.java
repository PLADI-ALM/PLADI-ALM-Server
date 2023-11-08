package com.example.pladialmserver.notification.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class PushNotification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long notificationId;

    @Column(nullable = false)
    private String notificationType;

    @Column(nullable = false)
    private String notificationInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="userId")
    private User user;

    @Builder
    public PushNotification(String notificationType, String notificationInfo, User user) {
        this.notificationType = notificationType;
        this.notificationInfo = notificationInfo;
        this.user = user;
    }

    public static PushNotification toEntity(String notificationType, String messageBody, User user) {
        return PushNotification.builder()
                .notificationType(notificationType)
                .notificationInfo(messageBody)
                .user(user)
                .build();
    }
}
