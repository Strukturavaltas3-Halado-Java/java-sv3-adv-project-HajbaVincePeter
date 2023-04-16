package com.example.restpost.exception.exceptions;

public class NoShipmentWithIdException extends RuntimeException {
    public NoShipmentWithIdException(Long id) {
        super(String.format("There is no shipment with the id: %s.",id));
    }
}
