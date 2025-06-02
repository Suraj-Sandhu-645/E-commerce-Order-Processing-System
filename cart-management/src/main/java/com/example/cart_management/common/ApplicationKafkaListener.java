package com.example.cart_management.common;


import com.example.cart_management.dto.OrderPlaced;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@KafkaListener
public class ApplicationKafkaListener {

    public KafkaTemplate<String, OrderPlaced> orderPlacedEvent;

    public ApplicationKafkaListener(KafkaTemplate<String, OrderPlaced> orderPlacedEvent) {
        this.orderPlacedEvent = orderPlacedEvent;
    }

    public void sendOrderPlaced(OrderPlaced orderPlaced) {
        orderPlacedEvent.send("order-placed", orderPlaced.getOrderId(), orderPlaced);
    }
}
