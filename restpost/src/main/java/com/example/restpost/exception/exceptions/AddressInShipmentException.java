package com.example.restpost.exception.exceptions;

public class AddressInShipmentException extends RuntimeException{

    public AddressInShipmentException(int numberOfShipments, Long id) {
        super(String.format("There is(are) %s shipment(s) containing the address with id: %s",numberOfShipments,id));
    }
}
