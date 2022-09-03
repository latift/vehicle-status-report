package com.example.vehiclestatus.exception;

public class InsuranceServiceNotAvailable extends RuntimeException{
    public InsuranceServiceNotAvailable(String message) {
        super(message);
    }
}
