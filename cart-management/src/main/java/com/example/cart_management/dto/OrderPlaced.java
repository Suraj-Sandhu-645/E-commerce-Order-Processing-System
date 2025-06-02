package com.example.cart_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPlaced {
    private String orderId;
    private int userId;
    private List<CartItemDTO> items;
    private double totalAmount;
    private String status; // e.g., "PENDING", "PLACED"
}
