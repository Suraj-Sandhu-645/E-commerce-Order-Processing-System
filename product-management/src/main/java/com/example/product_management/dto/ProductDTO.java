package com.example.product_management.dto;

import lombok.*;


@AllArgsConstructor
@Getter
@RequiredArgsConstructor
@Setter
public class ProductDTO {
    String productId;
    String name;
    String description;
    double price;
    int stock;
}


//"id": "string",
//        "name": "string",
//        "description": "string",
//        "price": "double",
//        "stock": "int"