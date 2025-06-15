package com.example.payment_simulation.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCompleted {
    private String orderId;
    private PaymentStatus status;
    private Instant timestamp;
}
