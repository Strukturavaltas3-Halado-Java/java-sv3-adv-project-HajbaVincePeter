package com.example.restpost.dtos.address_commands;

import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.County;
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
public class UpdateIrishCommand implements UpdateCommand {

    @NotNull
    private String streetAddress;
    @NotNull
    private String name;
    @NotNull
    private String City;
    @NotNull
    private County county;
}
