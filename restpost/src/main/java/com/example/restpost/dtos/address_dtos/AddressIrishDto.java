package com.example.restpost.dtos.address_dtos;

import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.County;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressIrishDto extends AddressDto {

    private Long id;

    private Country country;

    private String streetAddress;

    private String name;

    private String city;

    private County county;
}
