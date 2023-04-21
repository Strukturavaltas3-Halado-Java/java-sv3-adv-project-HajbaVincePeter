package com.example.restpost.mapper;


import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.dtos.address_dtos.AddressIrishDto;
import com.example.restpost.dtos.address_dtos.AddressWithPostalCodeDto;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.address.AddressWithPostalCode;
import com.example.restpost.model.address.AddressIrish;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toDto(Address address);

    AddressIrishDto toDto(AddressIrish addressIrish);

    AddressWithPostalCodeDto toDto(AddressWithPostalCode addressWithPostalCode);




}
