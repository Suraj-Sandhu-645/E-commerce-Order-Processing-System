package com.example.cart_management.controller;

import com.example.cart_management.dto.CartDTO;
import com.example.cart_management.dto.CartItemDTO;
import com.example.cart_management.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody CartItemDTO cartItemDTO, @PathVariable("userId") String userId){
        return new ResponseEntity<>(cartService.addItemToCart(cartItemDTO, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartDetailsByUserId(@PathVariable("userId") String userId){
        return new ResponseEntity<>(cartService.getCartDetailsById(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/remove")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable("userId") String userId, @RequestBody CartItemDTO cartItemDTO)  {
        return new ResponseEntity<>(cartService.removeItemToCart(cartItemDTO, userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<CartDTO> checkoutAndSendEvent(@PathVariable("userId") String userId){
        return new ResponseEntity<>(cartService.checkoutCartDetailBySendingEvent(userId), HttpStatus.OK);
    }
}
