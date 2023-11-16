package com.example.pladialmserver.notification.controller;

import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.notification.service.PushNotificationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "알림 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final PushNotificationService notificationService;

    @Scheduled(fixedDelay = 1000)
    @GetMapping("/")
    public ResponseCustom sendAssetsNotification() {
        notificationService.sendAssetsNotification();
        return ResponseCustom.OK();
    }
}
