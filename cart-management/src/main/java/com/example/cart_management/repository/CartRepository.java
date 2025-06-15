package com.example.cart_management.repository;

import com.example.cart_management.entity.CartEntity;
import com.example.cart_management.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,String> {
    Optional<CartEntity> findByUserId(String userId);
    boolean deleteByUserId(String userId);
}
