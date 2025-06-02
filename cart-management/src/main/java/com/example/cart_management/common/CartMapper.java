package com.example.cart_management.common;

import com.example.cart_management.dto.CartDTO;
import com.example.cart_management.dto.CartItemDTO;
import com.example.cart_management.entity.CartEntity;
import com.example.cart_management.entity.CartItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDTO toDto(CartEntity cartEntity);
    CartEntity toEntity(CartDTO cartDTO);
    CartItemEntity toItemEntity(CartItemDTO cartItemDTO);
    CartItemDTO toDtoEntity(CartItemEntity cartItemEntity);
}
