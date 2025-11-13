package com.enrollment.notifications.consumer;

import com.enrollment.notifications.config.RabbitMQConfig;
import com.enrollment.notifications.dto.NotificationMessage;
import com.enrollment.notifications.service.EmailService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.ENROLLMENT_QUEUE)
    public void processEnrollmentNotification(
            NotificationMessage notification,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            Message message
    ) throws IOException {

        logger.info("Procesando notificacion de matricula: estudiante={}, curso={}",
                notification.getStudentName(), notification.getCourseName());

        try {
            // Generar contenido del email
            String subject = "Confirmacion de Matricula - " + notification.getCourseName();
            String body = buildEmailBody(notification);

            // Enviar email
            emailService.sendEmail(notification.getStudentEmail(), subject, body);

            // Confirmar procesamiento exitoso
            channel.basicAck(deliveryTag, false);
            logger.info("Notificacion enviada exitosamente a {}", notification.getStudentEmail());

        } catch (Exception e) {
            logger.error("Error al procesar notificacion: {}", e.getMessage(), e);

            // Obtener contador de reintentos
            Integer retryCount = (Integer) message.getMessageProperties()
                    .getHeaders().get("x-retry-count");
            if (retryCount == null) {
                retryCount = 0;
            }

            if (retryCount < 3) {
                logger.warn("Reintentando notificacion (intento {}/3)", retryCount + 1);
                message.getMessageProperties().getHeaders().put("x-retry-count", retryCount + 1);
                channel.basicNack(deliveryTag, false, true);
            } else {
                logger.error("Notificacion enviada a DLQ despues de 3 intentos fallidos");
                channel.basicNack(deliveryTag, false, false);
            }
        }
    }

    @RabbitListener(queues = RabbitMQConfig.ENROLLMENT_DLQ)
    public void processDeadLetterQueue(NotificationMessage notification, Channel channel,
                                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        logger.error("Notificacion en DLQ - No pudo ser enviada: estudiante={}, email={}",
                notification.getStudentName(), notification.getStudentEmail());
        channel.basicAck(deliveryTag, false);
    }

    private String buildEmailBody(NotificationMessage notification) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = notification.getEnrollmentDate().format(formatter);

        return String.format("""
                Estimado/a %s,

                Te confirmamos que has sido matriculado exitosamente en el curso:

                Curso: %s
                Instructor: %s
                Fecha de matricula: %s
                Codigo de matricula: %s
               
                Saludos,
                Sistema de Matriculas
                """,
                notification.getStudentName(),
                notification.getCourseName(),
                notification.getInstructor(),
                formattedDate,
                notification.getEnrollmentId()
        );
    }
}
