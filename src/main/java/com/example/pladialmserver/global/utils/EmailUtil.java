package com.example.pladialmserver.global.utils;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

import static com.example.pladialmserver.global.exception.BaseResponseCode.BLACKLIST_EMAIL_CODE;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final SpringTemplateEngine templateEngine;

    private static final long ACCESS_CODE_EXPIRE_TIME = 1000 * 60 * 5; // 5분

    public void sendEmail(String toEmail) {
        String title = "(주) 플레디 사내 시스템 이메일 인증 번호";
        String code = RandomStringUtils.random(5, true, true);

        try{
            emailSender.send(createEmailForm(toEmail, title, code));
            redisUtil.setValue(toEmail, code, ACCESS_CODE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        } catch (RuntimeException e){
            throw new BaseException(BaseResponseCode.CAN_NOT_SEND_EMAIL);
        }
    }

    private MimeMessage createEmailForm(String toEmail, String title, String text) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(setContext("emailCode", text, "email"), true);
            return mimeMessage;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String verifiedCode(String toEmail){
        String key = redisUtil.getValue(toEmail);
        if(ObjectUtils.isEmpty(key)) throw new BaseException(BLACKLIST_EMAIL_CODE);
        return key;
    }

    public String setContext(String name, String code, String type) {
        Context context = new Context();
        context.setVariable(name, code);
        return templateEngine.process(type, context);
    }
}
