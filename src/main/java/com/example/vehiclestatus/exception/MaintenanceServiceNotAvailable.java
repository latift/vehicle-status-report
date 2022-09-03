package com.example.vehiclestatus.exception;

public class MaintenanceServiceNotAvailable extends RuntimeException {
    public MaintenanceServiceNotAvailable(String message) {
        super(message);
    }
}
