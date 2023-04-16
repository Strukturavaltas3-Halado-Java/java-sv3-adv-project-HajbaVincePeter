package com.example.restpost.exception;

import com.example.restpost.exception.exceptions.*;
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
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("wrong-data"));
        return detail;
    }


    @ExceptionHandler(CountryMismatchException.class)
    public ProblemDetail handleCountryMismatch(CountryMismatchException e) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("addresses/not-for-this-country"));
        return detail;
    }

    @ExceptionHandler(NoAddressWithIdException.class)
    public ProblemDetail handleNoAddressWithId(NoAddressWithIdException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("addresses/address-not-found"));
        return detail;
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("addresses/value-not-valid"));
        return detail;

    }


    @ExceptionHandler(NoPackageWithIdException.class)
    public ProblemDetail handleNoPackageWithIdException(NoPackageWithIdException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("packages/not-found"));
        return detail;
    }

    @ExceptionHandler(NoShipmentWithIdException.class)
    public ProblemDetail handleNoShipmentWithIdExtension(NoShipmentWithIdException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("shipments/not-found"));
        return detail;
    }

    @ExceptionHandler(AddressInShipmentException.class)
    public ProblemDetail handleAddressInShipmentException(AddressInShipmentException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("addresses/in-use"));
        return detail;
    }

    @ExceptionHandler(ShipmentAlreadyProcessedError.class)
    public ProblemDetail handleShipmentAlreadyProcessedError(ShipmentAlreadyProcessedError e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("shipments/processed"));
        return detail;
    }

    @ExceptionHandler(ShipmentNotCompleteError.class)
    public ProblemDetail handleShipmentNotCompleteError(ShipmentNotCompleteError e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("shipments/not-complete"));
        return detail;
    }


    @ExceptionHandler(NoShipmentWithTrackingNumberException.class)
    public ProblemDetail handleNoShipmentWithTrackingNumber(NoShipmentWithTrackingNumberException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        detail.setType(URI.create("shipment/tracking"));
        return detail;
    }

    @ExceptionHandler(NotFutureDateException.class)
    public ProblemDetail handleNotFutureDateException(NotFutureDateException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE,e.getMessage());
        detail.setType(URI.create("shipment/not-future"));
        return detail;
    }

    @ExceptionHandler(PackageNotInShipmentException.class)
    public ProblemDetail handlePackageNotInShipmentException(PackageNotInShipmentException e){
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        detail.setType(URI.create("package/wrong-data"));
        return detail;
    }

}
