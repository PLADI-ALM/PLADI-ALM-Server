package com.example.pladialmserver.notification.entity;

import com.example.pladialmserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long notificationIdx;

    @Column(nullable = false)
    private String notificationType;

    @Column(nullable = false)
    private String notificationInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="userIdx")
    private User user;

    @Builder
    public Notification(String notificationType, String notificationInfo, User user) {
        this.notificationType = notificationType;
        this.notificationInfo = notificationInfo;
        this.user = user;
    }
}
