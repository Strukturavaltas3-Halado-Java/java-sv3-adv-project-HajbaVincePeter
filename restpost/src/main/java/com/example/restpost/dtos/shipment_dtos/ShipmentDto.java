package com.example.restpost.dtos.shipment_dtos;

import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.packages.Package;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.val;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
