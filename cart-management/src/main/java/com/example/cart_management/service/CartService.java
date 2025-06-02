package com.example.cart_management.service;

import com.example.cart_management.common.ApplicationKafkaListener;
import com.example.cart_management.common.CartMapper;
import com.example.cart_management.dto.CartDTO;
import com.example.cart_management.dto.CartItemDTO;
import com.example.cart_management.dto.OrderPlaced;
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

    public CartDTO addItemToCart(CartItemDTO cartItemDTO, int userId) {

        Optional<CartEntity> cartEntityOptional = cartRepository.findById(userId);
        if (cartEntityOptional.isPresent()) {
            CartEntity cartEntity = cartEntityOptional.get();
            Optional<CartItemEntity> cartItemEntityOptional = cartEntity
                    .getCartItemEntity()
                    .stream()
                    .filter(a -> a.getProductId() == cartItemDTO.getProductId()).findFirst();

            if (cartItemEntityOptional.isPresent()) {
                cartItemEntityOptional.get().setQuantity(cartItemEntityOptional.get().getQuantity() + cartItemDTO.getQuantity());
                cartRepository.save(cartEntity);
            } else {
                CartItemEntity cartItemEntity = cartMapper.toItemEntity(cartItemDTO);
                cartEntity.getCartItemEntity().add(cartItemEntity);
                cartRepository.save(cartEntity);
            }

            return cartMapper.toDto(cartEntity);
        } else {
            CartDTO cartDTO = new CartDTO();
            List<CartItemDTO> cartItemDTOS = new ArrayList<>();
            cartItemDTOS.add(cartItemDTO);
            CartEntity cartEntity = cartMapper.toEntity(cartDTO);
            cartEntity = cartRepository.save(cartEntity);
            return cartMapper.toDto(cartEntity);
        }
    }

    public CartDTO removeItemToCart(CartItemDTO cartItemDTO, int userId) {
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(userId);
        if (cartEntityOptional.isPresent()) {
            CartEntity cartEntity = cartEntityOptional.get();
            List<CartItemEntity> cartItemEntity = cartEntity
                    .getCartItemEntity()
                    .stream()
                    .filter(a -> a.getProductId() != cartItemDTO.getProductId()).toList();

            if (cartItemEntity.isEmpty()) {

                throw new NoProductIdFoundException("No product id is present in database");
            } else {
                cartEntity.setCartItemEntity(cartItemEntity);
                cartRepository.save(cartEntity);
                return cartMapper.toDto(cartEntity);
            }
        } else {
            throw new NoUserIdFoundException("No user id is present in database");
        }
    }

    public CartDTO getCartDetailsById(int userId) {
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(userId);
        if (cartEntityOptional.isPresent()) {
            return cartMapper.toDto(cartEntityOptional.get());
        }else {
            throw new NoUserIdFoundException("No user id is present in database "+ userId);
        }
    }

    public CartDTO checkoutCartDetailBySendingEvent(int userId) {
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(userId);
        if (cartEntityOptional.isPresent()) {
            List<CartItemDTO> cartItemDTOS = cartEntityOptional.get().getCartItemEntity()
                    .stream()
                    .map(a -> cartMapper.toDtoEntity(a)).toList();

            OrderPlaced orderPlaced = OrderPlaced.builder()
                    .orderId(UUID.randomUUID().toString())
                    .items(cartItemDTOS)
                    .userId(userId)
                    .build();
            applicationKafkaListener.sendOrderPlaced(orderPlaced);

            return cartMapper.toDto(cartEntityOptional.get());
        } else {
            throw new NoUserIdFoundException("No user id is present in database "+ userId);
        }
    }
}
