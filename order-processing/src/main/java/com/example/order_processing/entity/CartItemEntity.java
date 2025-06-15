package com.example.order_processing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "order_cartItem")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemEntity {
    @Id
    private String productId;
    private int quantity;
    private double price;
}
