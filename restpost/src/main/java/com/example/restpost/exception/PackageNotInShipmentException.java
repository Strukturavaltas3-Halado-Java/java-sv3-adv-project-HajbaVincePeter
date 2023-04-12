package com.example.restpost.exception;

public class PackageNotInShipmentException extends RuntimeException {
    public PackageNotInShipmentException(Long id, Long shipmentId) {
        super(String.format("The package with id: %s is not in the shipment with id: %s.",id,shipmentId ));

    }
}
