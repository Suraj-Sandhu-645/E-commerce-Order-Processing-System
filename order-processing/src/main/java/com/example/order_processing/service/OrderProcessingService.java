package com.example.order_processing.service;

import com.example.order_processing.dto.OrderDTO;
import com.example.order_processing.dto.PaymentMode;
import com.example.order_processing.entity.CartItemEntity;
import com.example.order_processing.entity.OrderEntity;
import com.example.order_processing.exception.NoOrderIdPresentException;
import com.example.order_processing.exception.OrderIsNotPresentException;
import com.example.order_processing.incoming.OrderPlacedEvent;
import com.example.order_processing.incoming.PaymentCompleted;
import com.example.order_processing.outgoing.InventoryUpdated;
import com.example.order_processing.outgoing.PaymentRequested;
import com.example.order_processing.publisher.EventPublisher;
import com.example.order_processing.repository.OrderProcessingRepository;
import com.example.order_processing.util.OrderProcessingMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class OrderProcessingService {

    @Autowired
    OrderProcessingRepository orderProcessingRepository;
    @Autowired
    OrderProcessingMapper orderProcessingMapper;
    @Autowired
    EventPublisher eventPublisher;
    @Autowired
    RestTemplate restTemplate;

    public OrderDTO createOrder(OrderPlacedEvent orderPlacedEvent) {
        OrderEntity orderEntity = orderProcessingMapper.toOrderEntity(orderPlacedEvent);
        if(orderEntity != null) {
            orderEntity.setOrderId(orderPlacedEvent.getOrderId());
            orderEntity = orderProcessingRepository.save(orderEntity);
            log.info("Order data is persisted in database");
            PaymentRequested paymentRequested = PaymentRequested.builder()
                    .orderId(orderPlacedEvent.getOrderId())
                    .userId(orderPlacedEvent.getUserId())
                    .totalAmount(orderPlacedEvent.getTotalAmount())
                    .paymentMode(PaymentMode.COD)
                    .timestamp(Instant.now())
                    .build();

            eventPublisher.sendPaymentRequestedEvent(paymentRequested, "PaymentRequested");

            log.info("Initiated payment process");

            return orderProcessingMapper.toOrderDTO(orderEntity);
        }
        throw new NullPointerException("Order is null");
    }

    public OrderDTO getOrderDetailsByOrderId(String orderId) {
        if(orderId == null ) {
            throw new NoOrderIdPresentException("Requested orderId is null");
        } else {
            Optional<OrderEntity> orderEntityOptional = orderProcessingRepository.findByOrderId(orderId);
            if(orderEntityOptional.isPresent()){
                OrderDTO orderDTO = orderProcessingMapper.toOrderDTO(orderEntityOptional.get());
                return orderDTO;
            } else{
                throw new OrderIsNotPresentException("Order is not present with Order Id : " + orderId);
            }
        }
    }

    public List<OrderDTO> getAllOrderDetails() {
        List<OrderEntity> orderEntityList = orderProcessingRepository.findAll();
        if(orderEntityList.isEmpty()) {
            throw new OrderIsNotPresentException("No order is present in database");
        }
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(OrderEntity orderEntity: orderEntityList) {
            orderDTOList.add(orderProcessingMapper.toOrderDTO(orderEntity));
        }

        return orderDTOList;
    }

    public void processPaymentCompletedAndPublishInventoryUpdateEvent(PaymentCompleted paymentCompleted) {
        Optional<OrderEntity> orderEntityOptional = orderProcessingRepository.findByOrderId(paymentCompleted.getOrderId());

        if(orderEntityOptional.isPresent()) {
            List<CartItemEntity> cartItemEntityList = orderEntityOptional.get().getItems();
            for(CartItemEntity cartItemEntity : cartItemEntityList) {
                InventoryUpdated inventory = InventoryUpdated.builder()
                        .orderId(paymentCompleted.getOrderId())
                        .productId(cartItemEntity.getProductId())
                        .quantityChanged(cartItemEntity.getQuantity())
                        .timestamp(Instant.now())
                        .build();

                eventPublisher.sendInventoryUpdatedEvent(inventory, "InventoryUpdated");
            }
        }
    }

}
