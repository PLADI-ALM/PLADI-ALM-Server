package com.example.pladialmserver.notification.service;

import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.notification.dto.FcmMessage;
import com.example.pladialmserver.notification.entity.PushNotification;
import com.example.pladialmserver.notification.repository.PushNotificationRepository;
import com.example.pladialmserver.user.dto.request.SendAssetsEmailReq;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.example.pladialmserver.global.Constants.EmailNotification.*;


@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PushNotificationService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/pladi-alm-7702202947/messages:send";
    private final ObjectMapper objectMapper;
    private final PushNotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailUtil emailUtil;

    @Transactional
    public void sendNotification(String category, String type, User user) throws IOException {
        String title = getTitle(category, type);
        String messageBody = category + type;
        if (user.getFcmToken() != null) {
            FcmMessage fcmMessage = FcmMessage.makeMessage(user.getFcmToken(), title, messageBody);
            Response response = sendMessage(objectMapper.writeValueAsString(fcmMessage));
            notificationRepository.save(PushNotification.toEntity(title, messageBody, user));
        }
    }

    @Transactional
    public void sendNotification(User user, String title, String messageBody) throws IOException {
        if (user.getFcmToken() != null) {
            FcmMessage fcmMessage = FcmMessage.makeMessage(user.getFcmToken(), title, messageBody);
            Response response = sendMessage(objectMapper.writeValueAsString(fcmMessage));
            notificationRepository.save(PushNotification.toEntity(title, messageBody, user));
        }
    }

    // 자산 정보 확인 알림 / 알림 전송
    @Scheduled(cron = "0 0 9 1 */3 ?", zone = "GMT+9:00")
    public void sendAssetsNotification(){
        String title = Constants.NotificationCategory.ASSETS_TITLE;
        userRepository.findByAssetsIsNotNull().forEach(user -> {
            try {
                sendNotification(user, title, extractAssetsMessageBody(user.getAssets()));
            } catch (IOException e) {
                throw new BaseException(BaseResponseCode.NOTIFICATION_TOKEN_NOT_FOUND);
            }
        });
    }

    // 자산 정보 확인 알림 / 메일 전송
    @Scheduled(cron = "0 0 9 1 */3 ?", zone = "GMT+9:00")
    public void sendAssetsEmail(){
        userRepository.findByAssetsIsNotNull().forEach(user -> {
            emailUtil.sendEmail(user.getEmail(), COMPANY_NAME + ASSETS_TITLE
                    , emailUtil.createAssetsData(SendAssetsEmailReq.toDto(user)), ASSETS_TEMPLATE);
        });
    }

    private String extractAssetsMessageBody(String assets) {
        return Constants.NotificationCategory.ASSETS_INDIVIDUAL + assets + Constants.NotificationCategory.ASSETS_CHECK_MESSAGE;
    }

    private String getTitle(String category, String type) {
        String title = "";
        if (category.equals(Constants.NotificationCategory.CAR)) title = extract(type, title, category);
        if (category.equals(Constants.NotificationCategory.RESOURCE)) title = extract(type, title, category);
        if (category.equals(Constants.NotificationCategory.OFFICE)) title = extract(type, title, category);
        return title;
    }

    private String extract(String type, String title, String category) {
        if(type.contains(Constants.Notification.SUCCESS)) title = category + Constants.Notification.TITLE_SUCCESS;
        if(type.contains(Constants.Notification.DENIED)) title = category + Constants.Notification.TITLE_DENIED;
        if(type.contains(Constants.Notification.CANCEL)) title = category + Constants.Notification.TITLE_CANCELED;
        if(type.contains(Constants.Notification.RETURN)) title = category + Constants.Notification.TITLE_RETURNED;
        return title;
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
