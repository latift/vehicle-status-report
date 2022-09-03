package com.example.vehiclestatus.exception;

public class UnknownMaintenanceScore extends RuntimeException{
    public UnknownMaintenanceScore(String message) {
        super(message);
    }
}
