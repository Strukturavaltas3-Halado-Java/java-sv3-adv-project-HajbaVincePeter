package com.example.restpost.dtos.package_commands;


import jakarta.validation.constraints.Max;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class CreatePackageCommand {

    @NotNull(message = "The weight cannot be null.")
    @Positive(message = "A package has a positive weight.")
    @Max(value = 100, message = "A package cannot weight more than 100 kg.")
    private Integer weight;

}
