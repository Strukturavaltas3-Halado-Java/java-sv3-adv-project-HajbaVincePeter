package com.example.restpost.dtos;

import com.example.restpost.model.address.Country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;

    private Country country;

    private String streetAddress;

    private String name;

}
