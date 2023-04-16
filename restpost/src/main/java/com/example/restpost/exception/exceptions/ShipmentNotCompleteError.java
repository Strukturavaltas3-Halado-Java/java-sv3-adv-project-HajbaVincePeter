package com.example.restpost.exception.exceptions;

public class ShipmentNotCompleteError extends RuntimeException {
    public ShipmentNotCompleteError(long id) {

        super(String.format("Please complete the shipment details first for the shipment with id: %s!",id));

    }

}
