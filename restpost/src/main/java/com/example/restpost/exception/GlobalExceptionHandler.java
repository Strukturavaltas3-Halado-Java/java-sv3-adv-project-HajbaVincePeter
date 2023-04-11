package com.example.restpost.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException e){

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
        detail.setType(URI.create("addresses/wrong-data"));
        return detail;
    }


    @ExceptionHandler(CountryMismatchException.class)
    public ProblemDetail handleCountryMismatch(CountryMismatchException e){

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
        detail.setType(URI.create("addresses/not-for-this-country"));
        return detail;
    }

    @ExceptionHandler(NoAddressWithIdException.class)
    public ProblemDetail handleNoAddressWithId(NoAddressWithIdException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,e.getMessage());
        detail.setType(URI.create("addresses/address-not-found"));
        return  detail;
    }


    @ExceptionHandler (HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
        detail.setType(URI.create("addresses/value-not-valid"));
        return  detail;

    }



}
