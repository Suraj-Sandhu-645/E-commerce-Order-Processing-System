package com.example.order_processing.listener;

import com.example.order_processing.incoming.OrderPlacedEvent;
import com.example.order_processing.incoming.PaymentCompleted;
import com.example.order_processing.service.OrderProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderProcessingListener {

    @Autowired
    OrderProcessingService orderProcessingService;

    @KafkaListener(topics = "order-placed", groupId = "order-processing-group")
    public void processOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
        log.info("Order is created with data " + orderProcessingService.createOrder(orderPlacedEvent));
    }

    @KafkaListener(topics = "payment-completed", groupId = "payment-processing-group")
    public void processPaymentCompletedEvent(PaymentCompleted paymentCompleted) {
        log.info("PaymentCompleted event is received {}", paymentCompleted);
        orderProcessingService.processPaymentCompletedAndPublishInventoryUpdateEvent(paymentCompleted);
    }

}
