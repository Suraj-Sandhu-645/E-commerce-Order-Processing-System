package com.example.product_management.incoming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryUpdated {
    private String productId;
    private int quantityChanged;
    private String orderId;
    private Instant timestamp;
}

