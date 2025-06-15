package com.example.order_processing.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
    private String orderId;
    private String userId;
    private List<CartItemDTO> items;
    private double totalAmount;
    private OrderStatus status;
}
