package com.example.restpost.dtos.shipment_dtos;

import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.dtos.package_dtos.PackageDto;

import lombok.Data;



import java.time.LocalDate;

import java.util.HashSet;

import java.util.Set;

@Data
public class ShipmentDto {

    private Long id;


    private String trackingNumber;


    private AddressDto from;


    private AddressDto to;


    private LocalDate shippingDate;


    private Set<PackageDto> packages = new HashSet<>();




}
