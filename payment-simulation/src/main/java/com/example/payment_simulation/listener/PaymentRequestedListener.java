package com.example.payment_simulation.listener;

import com.example.payment_simulation.incoming.PaymentRequested;
import com.example.payment_simulation.service.PaymentProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentRequestedListener {

    @Autowired
    PaymentProcessService paymentProcessService;

    @KafkaListener(topics = "payment-requested", groupId = "payment-group-id")
    public void processPaymentResquesteEvent(PaymentRequested paymentRequested) {
        log.info("PaymentRequested event is received {}", paymentRequested);
        paymentProcessService.processPaymentRequested(paymentRequested);
    }

}
