package com.example.cart_management.service;

import com.example.cart_management.common.ApplicationKafkaListener;
import com.example.cart_management.common.CartItemMapper;
import com.example.cart_management.common.CartMapper;
import com.example.cart_management.dto.CartDTO;
import com.example.cart_management.dto.CartItemDTO;
import com.example.cart_management.outgoing.OrderPlacedEvent;
import com.example.cart_management.dto.OrderStatus;
import com.example.cart_management.entity.CartEntity;
import com.example.cart_management.entity.CartItemEntity;
import com.example.cart_management.exception.NoProductIdFoundException;
import com.example.cart_management.exception.NoUserIdFoundException;
import com.example.cart_management.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ApplicationKafkaListener applicationKafkaListener;
    @Autowired
    CartItemMapper cartItemMapper;

    public CartDTO addItemToCart(CartItemDTO cartItemDTO, String userId) {

        Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(userId);
        String userUUID = UUID.randomUUID().toString();
//        String productUUID = UUID.randomUUID().toString();
        if (cartEntityOptional.isPresent()) {
            CartEntity cartEntity = cartEntityOptional.get();
            Optional<CartItemEntity> cartItemEntityOptional = cartEntity
                    .getCartItemEntity()
                    .stream()
                    .filter(a -> cartItemDTO.getProductId()!= null && a.getProductId().equals(cartItemDTO.getProductId())).findFirst();

            if (cartItemEntityOptional.isPresent()) {
                cartItemEntityOptional.get().setQuantity(cartItemEntityOptional.get().getQuantity() + cartItemDTO.getQuantity());
                cartRepository.save(cartEntity);
            } else {
                CartItemEntity cartItemEntity = cartItemMapper.toEntity(cartItemDTO);
//                cartItemEntity.setProductId(productUUID);
                cartEntity.getCartItemEntity().add(cartItemEntity);
                cartEntity.setUserId(userId);
                cartRepository.save(cartEntity);
            }

            return cartMapper.toDto(cartEntity);
        } else {
            CartDTO cartDTO = new CartDTO();
            List<CartItemDTO> cartItemDTOS = new ArrayList<>();
//            cartItemDTO.setProductId(productUUID);
            cartItemDTOS.add(cartItemDTO);
            cartDTO.setCartItemDTO(cartItemDTOS);
            cartDTO.setUserId(userUUID);
            CartEntity cartEntity = cartMapper.toEntity(cartDTO);
            cartEntity = cartRepository.save(cartEntity);
            return cartMapper.toDto(cartEntity);
        }
    }

    public CartDTO removeItemToCart(CartItemDTO cartItemDTO, String userId) {
        CartEntity cartEntity = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NoUserIdFoundException("No user ID is present in the database"));

        List<CartItemEntity> items = cartEntity.getCartItemEntity();
        Optional<CartItemEntity> itemOptional = items.stream()
                .filter(item -> item.getProductId().equals(cartItemDTO.getProductId()))
                .findFirst();

        if (itemOptional.isEmpty()) {
            throw new NoProductIdFoundException("Product ID not found in cart");
        }

        CartItemEntity cartItem = itemOptional.get();
        int currentQty = cartItem.getQuantity();
        int removeQty = cartItemDTO.getQuantity();

        if (removeQty >= currentQty) {
            items.remove(cartItem);
        } else {
            cartItem.setQuantity(currentQty - removeQty);
        }
        cartRepository.save(cartEntity);
        return cartMapper.toDto(cartEntity);
    }

    public CartDTO getCartDetailsById(String userId) {
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(userId);
        if (cartEntityOptional.isPresent()) {
            return cartMapper.toDto(cartEntityOptional.get());
        }else {
            throw new NoUserIdFoundException("No user id is present in database "+ userId);
        }
    }

    public CartDTO checkoutCartDetailBySendingEvent(String userId) {
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(userId);
        if (cartEntityOptional.isPresent()) {
            List<CartItemDTO> cartItemDTOS = cartEntityOptional.get().getCartItemEntity()
                    .stream()
                    .map(a -> cartItemMapper.toDto(a)).toList();

            double totalAmount = cartEntityOptional.get().getCartItemEntity()
                    .stream().mapToDouble(item-> item.getQuantity()* item.getPrice()).sum();

            OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.builder()
                    .orderId(UUID.randomUUID().toString())
                    .items(cartItemDTOS)
                    .userId(userId)
                    .status(OrderStatus.PENDING)
                    .totalAmount(totalAmount)
                    .build();
            applicationKafkaListener.sendOrderPlaced(orderPlacedEvent,"OrderPlacedEvent");

            return cartMapper.toDto(cartEntityOptional.get());
        } else {
            throw new NoUserIdFoundException("No user id is present in database "+ userId);
        }
    }
}
