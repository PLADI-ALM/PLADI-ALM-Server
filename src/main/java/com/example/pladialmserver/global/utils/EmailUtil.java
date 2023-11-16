package com.example.pladialmserver.global.utils;

import com.example.pladialmserver.booking.dto.request.SendEmailReq;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.user.dto.request.SendAssetsEmailReq;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.pladialmserver.global.Constants.EmailNotification.EMAIL_CODE;
import static com.example.pladialmserver.global.exception.BaseResponseCode.BLACKLIST_EMAIL_CODE;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final SpringTemplateEngine templateEngine;

    private static final long ACCESS_CODE_EXPIRE_TIME = 1000 * 60 * 5; // 5분

    public void sendEmail(String toEmail, String title, Map<String, String> data, String template) {
        try{
            emailSender.send(createForm(toEmail, title, data, template));
        } catch (RuntimeException e){
            throw new BaseException(BaseResponseCode.CAN_NOT_SEND_EMAIL);
        }
    }


    private MimeMessage createForm(String toEmail, String title, Map<String, String> data, String template) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(setContext(data, template), true);
            return mimeMessage;
        } catch (MessagingException e) {
            throw new BaseException(BaseResponseCode.CAN_NOT_SEND_EMAIL);
        }
    }

    // Thymeleaf 접근
    private String setContext(Map<String, String> data, String template) {
        Context context = new Context();
        context.setVariable(template, data);
        return templateEngine.process(template, context);
    }

    // 이메일 코드 레디스에 저장
    public void setEmailCodeInRedis(String toEmail, String code){
        redisUtil.setValue(toEmail, code, ACCESS_CODE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    // 이메일 인증 코드 확인
    public String verifiedCode(String toEmail){
        String key = redisUtil.getValue(toEmail);
        if(ObjectUtils.isEmpty(key)) throw new BaseException(BLACKLIST_EMAIL_CODE);
        return key;
    }

    public Map<String, String> createEmailCodeData(String code){
        Map<String, String> emailData = new HashMap<>();
        emailData.put(EMAIL_CODE, code);
        return emailData;
    }

    public Map<String, String> createBookingData(SendEmailReq emailReq){
        Map<String, String> bookingData = new HashMap<>();
        bookingData.put("text", emailReq.getText());
        bookingData.put("reservatorName", emailReq.getReservatorName());
        bookingData.put("officeProduct", emailReq.getOffice_product());
        bookingData.put("resourceName", emailReq.getProductName());
        bookingData.put("reservationTime", DateTimeUtil.dateTimeToStringNullable(emailReq.getStartDateTime()) + " ~ " + DateTimeUtil.dateTimeToStringNullable(emailReq.getEndDateTime()));
        return bookingData;
    }

    public Map<String, String> createAssetsData(SendAssetsEmailReq emailReq){
        Map<String, String> assetsData = new HashMap<>();
        assetsData.put("name", emailReq.getName());
        assetsData.put("affiliation", emailReq.getAffiliation());
        assetsData.put("department", emailReq.getDepartment());
        assetsData.put("assets", emailReq.getAssets());
        return assetsData;
    }


}
