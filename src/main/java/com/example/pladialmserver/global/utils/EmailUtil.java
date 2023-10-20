package com.example.pladialmserver.global.utils;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.example.pladialmserver.global.exception.BaseResponseCode.BLACKLIST_EMAIL_CODE;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;

    private static final long ACCESS_CODE_EXPIRE_TIME = 1000 * 60 * 5; // 5분

    public void sendEmail(String toEmail) {
        String title = "(주) 플레디 사내 시스템 이메일 인증 번호";
        String code = RandomStringUtils.random(5, true, true);
        String text = "인증번호는 " + code + " 입니다. ";

        try{
            emailSender.send(createEmailForm(toEmail, title, text));
            redisUtil.setValue(toEmail, code, ACCESS_CODE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        } catch (RuntimeException e){
            throw new BaseException(BaseResponseCode.CAN_NOT_SEND_EMAIL);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        return message;
    }

    public String verifiedCode(String toEmail){
        String key = redisUtil.getValue(toEmail);
        if(ObjectUtils.isEmpty(key)) throw new BaseException(BLACKLIST_EMAIL_CODE);
        return key;
    }
}
