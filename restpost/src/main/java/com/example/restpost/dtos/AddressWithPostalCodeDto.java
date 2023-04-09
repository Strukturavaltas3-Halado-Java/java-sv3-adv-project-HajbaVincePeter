package com.example.restpost.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressWithPostalCodeDto extends AddressDto{
    private Integer postalCode;

}
