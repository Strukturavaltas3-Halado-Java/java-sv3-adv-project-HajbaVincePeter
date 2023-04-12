package com.example.restpost.dtos.address_commands;

import com.example.restpost.model.address.Country;
import com.example.restpost.validator.ValidatePostalAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidatePostalAddress
public class UpdatePostalCommand implements UpdateCommand {
    @NotNull
    private Country country;
    @NotNull
    private String streetAddress;
    @NotNull
    private String name;
    @NotNull
    private String city;
    @NotNull
    private String postalCode;

}
