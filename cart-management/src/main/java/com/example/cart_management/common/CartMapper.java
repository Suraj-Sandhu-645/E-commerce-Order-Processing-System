package com.example.cart_management.common;

import com.example.cart_management.dto.CartDTO;
import com.example.cart_management.entity.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {
    @Mapping(source = "cartItemEntity", target = "cartItemDTO")
    CartDTO toDto(CartEntity cartEntity);

    @Mapping(source = "cartItemDTO", target = "cartItemEntity")
    CartEntity toEntity(CartDTO cartDTO);
}
