package com.example.restpost.dtos.package_commands;

import jakarta.validation.constraints.Max;
import lombok.Data;

@Data
public class UpdatePackageCommand {

    private Long id;

    @Max(value = 100, message = "A package cannot weight more than 100Kg.")
    private Integer weight;

    private Long shipmentId;

}
