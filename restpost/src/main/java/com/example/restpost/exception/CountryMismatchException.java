package com.example.restpost.exception;

public class CountryMismatchException extends RuntimeException {
    public CountryMismatchException(Long id) {

        super(String.format("The type of the country of the address with the id: %s cannot be updated with this update.",id));
    }
}
