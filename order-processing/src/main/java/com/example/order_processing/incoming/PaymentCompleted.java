package com.example.order_processing.incoming;

import com.example.order_processing.dto.PaymentStatus;
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
