package com.example.restpost.exception.exceptions;

public class PackageNotInShipmentException extends RuntimeException {
    public PackageNotInShipmentException(Long id, Long shipmentId) {
        super(String.format("Wrong shipment id: %s for the package with id: %s.",shipmentId,id));

    }
}
