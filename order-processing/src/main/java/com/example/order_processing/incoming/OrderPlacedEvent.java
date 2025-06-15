package com.example.order_processing.incoming;

import com.example.order_processing.dto.CartItemDTO;
import com.example.order_processing.dto.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPlacedEvent {
    private String orderId;
    private String userId;
    private List<CartItemDTO> items;
    private double totalAmount;
    private OrderStatus status; // e.g., "PENDING", "PLACED"
}
