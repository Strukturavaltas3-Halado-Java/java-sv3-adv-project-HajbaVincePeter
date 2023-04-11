package com.example.restpost.dtos.address_dtos;

import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.model.address.Country;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressWithPostalCodeDto extends AddressDto {

    private Long id;

    private Country country;

    private String streetAddress;

    private String name;

    private String city;

    private String postalCode;

}
