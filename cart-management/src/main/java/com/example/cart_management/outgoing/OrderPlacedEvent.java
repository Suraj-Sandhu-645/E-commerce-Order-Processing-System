package com.example.cart_management.outgoing;

import com.example.cart_management.dto.CartItemDTO;
import com.example.cart_management.dto.OrderStatus;
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
