package com.example.ecommerce.exception;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
