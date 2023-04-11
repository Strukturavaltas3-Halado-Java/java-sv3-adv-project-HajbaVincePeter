package com.example.restpost.dtos.address_commands;

import com.example.restpost.model.address.Country;
import com.example.restpost.validator.ValidatePostalAddress;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private Country country;
    @NotBlank
    private String streetAddress;
    @NotBlank
    private String name;
    @NotBlank
    private String city;
    @NotBlank
    private String postalCode;

}
