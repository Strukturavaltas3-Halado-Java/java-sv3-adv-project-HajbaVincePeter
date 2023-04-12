package com.example.restpost.dtos.package_commands;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class CreatePackageCommand {

    @Max(value = 100, message = "A package cannot weight more than 100Kg.")
    private Integer weight;

}
