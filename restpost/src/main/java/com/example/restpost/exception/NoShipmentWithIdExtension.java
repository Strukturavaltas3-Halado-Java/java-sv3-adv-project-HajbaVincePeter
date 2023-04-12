package com.example.restpost.exception;

public class NoShipmentWithIdExtension extends RuntimeException {
    public NoShipmentWithIdExtension(Long id) {
        super(String.format("There is no shipment with the id: %s.",id));
    }
}
