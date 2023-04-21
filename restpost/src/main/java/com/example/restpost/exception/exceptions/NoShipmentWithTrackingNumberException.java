package com.example.restpost.exception.exceptions;

public class NoShipmentWithTrackingNumberException extends RuntimeException {
    public NoShipmentWithTrackingNumberException(String trackingNumber) {

        super(String.format("The shipment %s cannot be found", trackingNumber));

    }
}
