package com.sparta.restplaceforj.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j(topic = "MailUtil")
@Component
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String configEmail;

    public void sendMail(String toEmail, Long planId, String authCode) throws MessagingException {
        mailSender.send(createEmailForm(toEmail, planId, authCode));
    }

    public MimeMessage createEmailForm(String toEmail, Long planId, String authCode) throws MessagingException {

        String inviteLink = "http://localhost:3000/v1/plans/" + planId + "?authCode=" + authCode;

        // 메세지 설정
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); // 보내는 대상
        message.setFrom(configEmail); //보내는 사람 메일 주소
        message.setSubject("[J의 안식처] 인증번호"); // 메일 제목

        String msg="";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">[J의 안식처] 여행 계획 공동 작업자 초대</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 링크로 접속해서 인증을 완료하세요!</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += inviteLink;
        msg += "</td></tr></tbody></table></div>";
        message.setText(msg, "utf-8", "html");

        return message;
    }
}
