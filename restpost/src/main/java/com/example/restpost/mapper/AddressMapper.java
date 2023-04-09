package com.example.restpost.mapper;

import com.example.restpost.dtos.AddressDto;
import com.example.restpost.dtos.AddressWithPostalCodeDto;
import com.example.restpost.dtos.IrishAddressDto;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.address.AddressWithPostalCode;
import com.example.restpost.model.address.IrishAddress;
import org.mapstruct.Mapper;

@Mapper
public interface AddressMapper {

    AddressDto toDto(Address address);

    IrishAddressDto toDto(IrishAddress irishAddress);

    AddressWithPostalCodeDto toDto(AddressWithPostalCode addressWithPostalCode);



}
