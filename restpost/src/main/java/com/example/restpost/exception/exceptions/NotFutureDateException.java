package com.example.restpost.exception.exceptions;

public class NotFutureDateException extends RuntimeException {
    public NotFutureDateException(long id) {
        super(String.format("The planned shipping date has to be in the future!"));
    }
}
