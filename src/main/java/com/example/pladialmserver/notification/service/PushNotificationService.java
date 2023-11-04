package com.example.pladialmserver.notification.service;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.notification.dto.FcmMessage;
import com.example.pladialmserver.notification.entity.PushNotification;
import com.example.pladialmserver.notification.repository.PushNotificationRepository;
import com.example.pladialmserver.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushNotificationService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/pladi-alm-7702202947/messages:send";
    private final ObjectMapper objectMapper;
    private final PushNotificationRepository notificationRepository;

    public void sendAllowBookingNotification(CarBooking carBooking, User user) throws IOException {
        String messageBody = carBooking.getCar().getName()+" 차량이 예약되었습니다.";
        createNotification(user, messageBody);
    }

    public void sendAllowBookingNotification(ResourceBooking resourceBooking, User user) throws IOException {
        String messageBody = resourceBooking.getResource().getName()+" 장비가 예약되었습니다.";
        createNotification(user, messageBody);
    }

    public void sendRejectBookingNotification(CarBooking carBooking, User user) throws IOException {
        String messageBody = carBooking.getCar().getName()+" 차량 예약이 반려되었습니다.";
        createNotification(user, messageBody);
    }

    public void sendRejectBookingNotification(ResourceBooking resourceBooking, User user) throws IOException {
        String messageBody = resourceBooking.getResource().getName()+" 장비 예약이 반려되었습니다.";
        createNotification(user, messageBody);
    }

    public void sendReturnBookingNotification(CarBooking carBooking, User user) throws IOException {
        String messageBody = carBooking.getCar().getName()+" 차량이 반납되었습니다.";
        createNotification(user, messageBody);
    }

    public void sendReturnBookingNotification(ResourceBooking resourceBooking, User user) throws IOException {
        String messageBody = resourceBooking.getResource().getName()+"장비가 반납되었습니다.";
        createNotification(user, messageBody);
    }

    public void sendCancelBookingNotification(CarBooking carBooking, User user) throws IOException {
        String messageBody = carBooking.getCar().getName()+" 차량 예약이 취소되었습니다.";
        createNotification(user, messageBody);
    }

    public void sendCancelBookingNotification(ResourceBooking resourceBooking, User user) throws IOException {
        String messageBody = resourceBooking.getResource().getName()+" 장비 예약이 취소되었습니다.";
        createNotification(user, messageBody);
    }

    public void sendCancelBookingNotification(OfficeBooking officeBooking, User user) throws IOException {
        String messageBody = officeBooking.getOffice().getName()+" 회의실 예약이 취소되었습니다.";
        createNotification(user, messageBody);
    }

    @Transactional
    public void createNotification(User user, String messageBody) throws IOException {
        if (user.getFcmToken() != null) {
            FcmMessage fcmMessage = FcmMessage.makeMessage(user.getFcmToken(), " ", messageBody);
            Response response = sendMessage(objectMapper.writeValueAsString(fcmMessage));
            notificationRepository.save(PushNotification.toEntity("", messageBody, user));
        }
    }

    @NotNull
    private Response sendMessage(String message) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        return client.newCall(request).execute();
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
