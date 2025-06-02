package com.example.product_management.exception;

public class MissingProductDataException extends RuntimeException{


    public MissingProductDataException(String message) {
        super(message);
    }
}
