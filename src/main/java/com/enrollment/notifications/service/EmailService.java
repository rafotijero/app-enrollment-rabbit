package com.enrollment.notifications.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            logger.info("Enviando email a: {}", to);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@enrollment.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            logger.info("Email enviado exitosamente a: {}", to);

        } catch (Exception e) {
            logger.error("Error al enviar email a {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Error al enviar email", e);
        }
    }
}
