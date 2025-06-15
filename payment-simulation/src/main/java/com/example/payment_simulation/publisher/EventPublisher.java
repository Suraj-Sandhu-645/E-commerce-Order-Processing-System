package com.example.payment_simulation.publisher;

import com.example.payment_simulation.dto.PaymentCompleted;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    public KafkaTemplate<String, PaymentCompleted> kafkaTemplate;

    public EventPublisher(KafkaTemplate<String, PaymentCompleted> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(PaymentCompleted paymentCompleted, String eventType) {
        Message<PaymentCompleted> message= MessageBuilder
                .withPayload(paymentCompleted)
                .setHeader(KafkaHeaders.TOPIC, "payment-completed")
                .setHeader("eventType", eventType)
                .build();
        kafkaTemplate.send(message);
    }
}
