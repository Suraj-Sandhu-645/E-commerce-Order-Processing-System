package com.example.order_processing.exception;

public class OrderIsNotPresentException extends RuntimeException{
    public OrderIsNotPresentException(String message) {
        super(message);
    }
}
