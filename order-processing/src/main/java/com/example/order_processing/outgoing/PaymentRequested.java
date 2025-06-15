package com.example.order_processing.outgoing;

import com.example.order_processing.dto.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequested {
    private String orderId;
    private String userId;
    double totalAmount;
    private PaymentMode paymentMode;
    private Instant timestamp;

}