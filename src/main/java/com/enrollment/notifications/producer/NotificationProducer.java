package com.enrollment.notifications.producer;

import com.enrollment.notifications.config.RabbitMQConfig;
import com.enrollment.notifications.dto.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEnrollmentNotification(NotificationMessage notification) {
        try {
            logger.info("Enviando notificacion de matricula a la cola: estudiante={}, curso={}",
                    notification.getStudentName(), notification.getCourseName());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ENROLLMENT_EXCHANGE,
                    RabbitMQConfig.ENROLLMENT_ROUTING_KEY,
                    notification
            );

            logger.info("Notificacion encolada exitosamente para {}", notification.getStudentEmail());

        } catch (Exception e) {
            logger.error("Error al encolar notificacion: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar notificacion a RabbitMQ", e);
        }
    }
}
