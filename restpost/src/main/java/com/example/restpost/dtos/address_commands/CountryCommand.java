package com.example.restpost.dtos.address_commands;

import com.example.restpost.model.address.Country;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryCommand {

    @NotBlank
    private Country country;

}
