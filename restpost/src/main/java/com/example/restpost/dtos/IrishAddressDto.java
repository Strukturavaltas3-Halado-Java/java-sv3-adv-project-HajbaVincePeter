package com.example.restpost.dtos;

import com.example.restpost.model.address.County;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IrishAddressDto extends AddressDto{

    private County county;

}
