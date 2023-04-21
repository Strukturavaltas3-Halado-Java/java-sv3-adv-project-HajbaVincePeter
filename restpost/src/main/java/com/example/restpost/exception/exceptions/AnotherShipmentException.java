package com.example.restpost.exception.exceptions;

public class AnotherShipmentException extends RuntimeException {
    public AnotherShipmentException(long packageId) {
        super(String. format("The package with id: %s is already in another shipment.",packageId));
    }
}
