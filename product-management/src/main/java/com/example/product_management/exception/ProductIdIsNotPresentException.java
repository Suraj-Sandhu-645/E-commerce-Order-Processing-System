package com.example.product_management.exception;

public class ProductIdIsNotPresentException extends RuntimeException{


    public ProductIdIsNotPresentException(String message) {
        super(message);
    }
}
