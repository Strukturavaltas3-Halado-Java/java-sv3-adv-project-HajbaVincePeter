package com.example.restpost.dtos.address_commands;

import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.County;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIrishCommand implements UpdateCommand {

    @NotBlank
    private String streetAddress;
    @NotBlank
    private String name;
    @NotBlank
    private String City;
    @NotBlank
    private County county;
}
