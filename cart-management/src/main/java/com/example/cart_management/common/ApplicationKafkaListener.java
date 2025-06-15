package com.example.cart_management.common;


import com.example.cart_management.outgoing.OrderPlacedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
public class ApplicationKafkaListener {

    public KafkaTemplate<String, OrderPlacedEvent> orderPlacedEvent;

    public ApplicationKafkaListener(KafkaTemplate<String, OrderPlacedEvent> orderPlacedEvent) {
        this.orderPlacedEvent = orderPlacedEvent;
    }

    public void sendOrderPlaced(OrderPlacedEvent event, String eventType) {
        Message<OrderPlacedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "order-placed")
                .setHeader("eventType", eventType)
                .build();

        this.orderPlacedEvent.send(message);
    }
}
