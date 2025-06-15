package com.example.product_management.listener;

import com.example.product_management.incoming.InventoryUpdated;
import com.example.product_management.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductProcessingListener {

    @Autowired
    ProductService productService;


    @KafkaListener(topics = "inventory-updated", groupId = "inventory-updated-group")
    public void processInventoryUpdatedEvent(InventoryUpdated inventoryUpdated) {
        log.info("InventoryUpdated event is consumed {}", inventoryUpdated);
        productService.processInventoryUpdatedEvent(inventoryUpdated);
    }
}
