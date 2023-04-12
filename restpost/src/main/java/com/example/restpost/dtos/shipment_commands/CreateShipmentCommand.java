package com.example.restpost.dtos.shipment_commands;

import com.example.restpost.model.address.Address;
import com.example.restpost.model.packages.Package;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class CreateShipmentCommand {

    // private String trackingNumber;

    private Long fromId;

    private Long toId;

    private LocalDate shippingDate;

    private Set<Long> packagesIdList;


}
