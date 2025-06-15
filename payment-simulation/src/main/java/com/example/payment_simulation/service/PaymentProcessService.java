package com.example.payment_simulation.service;

import com.example.payment_simulation.dto.PaymentCompleted;
import com.example.payment_simulation.dto.PaymentStatus;
import com.example.payment_simulation.incoming.PaymentRequested;
import com.example.payment_simulation.publisher.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class PaymentProcessService {
    @Autowired
    EventPublisher eventPublisher;

    public void processPaymentRequested(PaymentRequested paymentRequested){

        PaymentCompleted paymentCompleted = PaymentCompleted.builder()
                .orderId(paymentRequested.getOrderId())
                .timestamp(Instant.now())
                .status(PaymentStatus.COMPLETED)
                .build();

        eventPublisher.send(paymentCompleted, "PaymentCompleted");
        log.info("Event is published {}", paymentCompleted);
    }
}
