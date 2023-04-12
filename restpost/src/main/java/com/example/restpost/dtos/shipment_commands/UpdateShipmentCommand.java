package com.example.restpost.dtos.shipment_commands;

import com.example.restpost.model.address.Address;
import com.example.restpost.model.packages.Package;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.Future;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class UpdateShipmentCommand {

    private Long id;

   // private String trackingNumber;


    private Long fromId;


    private Long toId;

    @Future
    private LocalDate shippingDate;


    private Set<Long> packagesIdList;

}
