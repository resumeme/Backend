package org.devcourse.resumeme.business.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.mail.EmailInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendEmail(EmailInfo emailInfo) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        String html = templateEngine.process(emailInfo.type().templateName, new Context(Locale.KOREA, emailInfo.attributes()));

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(from);
            messageHelper.setTo(emailInfo.to());
            messageHelper.setSubject(emailInfo.type().subject);
            messageHelper.setText(html, true);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("""
                            Fail Send Email \
                            "To : {} \
                            Subject : {}\"
                            """
                    , emailInfo.to(), emailInfo.type().subject);
        }
        log.info("email sent successfully");
    }

}
