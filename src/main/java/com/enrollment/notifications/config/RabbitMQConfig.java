package com.enrollment.notifications.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ENROLLMENT_QUEUE = "enrollment.notification.queue";
    public static final String ENROLLMENT_DLQ = "enrollment.notification.dlq";
    public static final String ENROLLMENT_EXCHANGE = "enrollment.exchange";
    public static final String ENROLLMENT_ROUTING_KEY = "enrollment.notify";
    public static final String ENROLLMENT_DLQ_ROUTING_KEY = "enrollment.dlq";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue enrollmentQueue() {
        return QueueBuilder.durable(ENROLLMENT_QUEUE)
                .withArgument("x-dead-letter-exchange", ENROLLMENT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ENROLLMENT_DLQ_ROUTING_KEY)
                .withArgument("x-message-ttl", 600000)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(ENROLLMENT_DLQ).build();
    }

    @Bean
    public DirectExchange enrollmentExchange() {
        return new DirectExchange(ENROLLMENT_EXCHANGE);
    }

    @Bean
    public Binding enrollmentBinding(Queue enrollmentQueue, DirectExchange enrollmentExchange) {
        return BindingBuilder
                .bind(enrollmentQueue)
                .to(enrollmentExchange)
                .with(ENROLLMENT_ROUTING_KEY);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange enrollmentExchange) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(enrollmentExchange)
                .with(ENROLLMENT_DLQ_ROUTING_KEY);
    }
}
