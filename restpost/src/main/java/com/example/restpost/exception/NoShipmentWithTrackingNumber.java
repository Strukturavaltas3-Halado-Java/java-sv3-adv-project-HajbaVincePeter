package com.example.restpost.exception;

public class NoShipmentWithTrackingNumber extends RuntimeException {
    public NoShipmentWithTrackingNumber(String trackingNumber) {

        super(String.format("The shipment %s cannot be found",trackingNumber));

    }
}
