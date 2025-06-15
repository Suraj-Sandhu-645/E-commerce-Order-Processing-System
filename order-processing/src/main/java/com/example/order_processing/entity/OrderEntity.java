package com.example.order_processing.entity;

import com.example.order_processing.dto.CartItemDTO;
import com.example.order_processing.dto.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Getter
@Setter
public class OrderEntity {
    @Id
    @Column(name = "order_id")
    private String orderId;
    private String userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<CartItemEntity> items;
    private double totalAmount;
    private OrderStatus status;
}
