package com.example.cart_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class CartDTO {
    private String userId;
    private List<CartItemDTO> cartItemDTO;
}
