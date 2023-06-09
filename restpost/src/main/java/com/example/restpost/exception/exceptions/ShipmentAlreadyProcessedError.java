package com.example.restpost.exception.exceptions;

public class ShipmentAlreadyProcessedError extends RuntimeException {
    public ShipmentAlreadyProcessedError(long id) {
        super(String.format("The shipment with id: %s is already processed.",id));
    }
}
