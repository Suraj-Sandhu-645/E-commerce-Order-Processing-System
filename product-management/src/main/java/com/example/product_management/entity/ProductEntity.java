package com.example.product_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Entity
@Table(name = "product")
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductEntity {

    @Id
    @Column(name = "productId")
    String productId;
    String name;
    String description;
    double price;
    int stock;
}
