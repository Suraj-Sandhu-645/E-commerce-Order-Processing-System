package com.example.order_processing.util;

import com.example.order_processing.dto.OrderDTO;
import com.example.order_processing.entity.OrderEntity;
import com.example.order_processing.incoming.OrderPlacedEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface OrderProcessingMapper {

    OrderEntity toOrderEntity(OrderDTO orderDTO);
    OrderDTO toOrderDTO(OrderEntity orderEntity);
    OrderEntity toOrderEntity(OrderPlacedEvent orderPlacedEvent);
}
