package com.example.cart_management.exception;

public class NoProductIdFoundException extends RuntimeException{
    public NoProductIdFoundException(String mes) {
        super(mes);
    }
}
