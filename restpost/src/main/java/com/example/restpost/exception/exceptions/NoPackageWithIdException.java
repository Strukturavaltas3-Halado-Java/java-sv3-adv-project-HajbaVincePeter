package com.example.restpost.exception.exceptions;

public class NoPackageWithIdException extends RuntimeException {
    public NoPackageWithIdException(long id) {
        super(String.format("There is no package with the id: %s.",id));
    }
}
