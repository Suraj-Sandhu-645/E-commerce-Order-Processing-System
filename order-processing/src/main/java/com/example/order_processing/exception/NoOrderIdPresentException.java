package com.example.order_processing.exception;

public class NoOrderIdPresentException extends RuntimeException{

    public NoOrderIdPresentException(String message) {
        super(message);
    }
}
