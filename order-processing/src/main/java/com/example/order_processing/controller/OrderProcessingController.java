package com.example.order_processing.controller;

import com.example.order_processing.dto.OrderDTO;
import com.example.order_processing.incoming.OrderPlacedEvent;
import com.example.order_processing.service.OrderProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders/")
public class OrderProcessingController {

    @Autowired
    OrderProcessingService orderProcessingService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("orderId") String orderId){
        return new ResponseEntity<>(orderProcessingService.getOrderDetailsByOrderId(orderId), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders(){
        return new ResponseEntity<>(orderProcessingService.getAllOrderDetails(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<OrderDTO> createOrderInDatabase(@RequestBody OrderPlacedEvent orderDTO) {
        return new ResponseEntity<>(orderProcessingService.createOrder(orderDTO), HttpStatus.CREATED);
    }
}
