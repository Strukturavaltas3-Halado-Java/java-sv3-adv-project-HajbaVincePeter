package com.example.restpost.dtos.package_dtos;

import com.example.restpost.model.shipment.Shipment;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class PackageDto {

    private Long id;

    private Integer weight;

    private Long shipmentId;


}
