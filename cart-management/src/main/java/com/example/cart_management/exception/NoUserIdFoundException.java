package com.example.cart_management.exception;

public class NoUserIdFoundException extends RuntimeException{
    public NoUserIdFoundException(String mes) {
        super(mes);
    }
}
