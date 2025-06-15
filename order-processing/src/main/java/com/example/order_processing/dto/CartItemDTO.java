package com.example.order_processing.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private String productId;
    private int quantity;
    private double price;
}
