package com.example.order_processing.repository;

import com.example.order_processing.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderProcessingRepository extends JpaRepository<OrderEntity, String> {
    public Optional<OrderEntity> findByOrderId(String orderId);
}
