package com.example.order_processing.publisher;

import com.example.order_processing.outgoing.InventoryUpdated;
import com.example.order_processing.outgoing.PaymentRequested;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    public KafkaTemplate<String, PaymentRequested> paymentRequestedKafkaTemplate;
    public KafkaTemplate<String, InventoryUpdated> inventoryKafkaTemplate;

    public EventPublisher(KafkaTemplate<String, PaymentRequested> paymentRequestedKafkaTemplate,
                          KafkaTemplate<String, InventoryUpdated> inventoryKafkaTemplate) {
        this.paymentRequestedKafkaTemplate = paymentRequestedKafkaTemplate;
        this.inventoryKafkaTemplate = inventoryKafkaTemplate;
    }

    public void sendPaymentRequestedEvent(PaymentRequested event, String eventType) {
        Message<PaymentRequested> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "payment-requested")
                .setHeader("eventType", eventType)
                .build();

        paymentRequestedKafkaTemplate.send(message);
    }

    public void sendInventoryUpdatedEvent(InventoryUpdated inventoryUpdated, String eventType) {
        Message<InventoryUpdated> message = MessageBuilder
                .withPayload(inventoryUpdated)
                .setHeader(KafkaHeaders.TOPIC,"inventory-updated")
                .setHeader("eventType", eventType)
                .build();

        inventoryKafkaTemplate.send(message);
    }
}
