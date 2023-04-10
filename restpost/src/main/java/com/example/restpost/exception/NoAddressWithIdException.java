package com.example.restpost.exception;

public class NoAddressWithIdException extends RuntimeException {
    public NoAddressWithIdException(Long id) {
        super(String.format("The address with the id %s does not exist", id));
    }
}
