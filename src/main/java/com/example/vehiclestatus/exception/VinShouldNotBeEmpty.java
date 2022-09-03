package com.example.vehiclestatus.exception;

public class VinShouldNotBeEmpty extends RuntimeException{
    public VinShouldNotBeEmpty(String message) {
        super(message);
    }
}
