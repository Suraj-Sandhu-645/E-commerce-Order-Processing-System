package com.example.cart_management.common;

import com.example.cart_management.dto.CartItemDTO;
import com.example.cart_management.entity.CartItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
//MapStruct automatically generates an implementation called CartMapperImpl
// and registers it as a Spring-managed bean. You do not need to manually create it.
public interface CartItemMapper {
    CartItemEntity toEntity(CartItemDTO cartItemDTO);
    CartItemDTO toDto(CartItemEntity cartItemEntity);
}
