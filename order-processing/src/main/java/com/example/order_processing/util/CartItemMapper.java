package com.example.order_processing.util;

import com.example.order_processing.dto.CartItemDTO;
import com.example.order_processing.entity.CartItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemEntity toItemEntity(CartItemDTO cartItemDTO);
    CartItemDTO toItemDTO(CartItemEntity cartItemEntity);
}
