package com.example.restpost.service;

import com.example.restpost.dtos.AddressDto;
import com.example.restpost.dtos.AddressWithPostalCodeDto;
import com.example.restpost.dtos.CountryCommand;
import com.example.restpost.mapper.AddressMapper;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.address.AddressWithPostalCode;
import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.IrishAddress;
import com.example.restpost.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressRepository addressRepository;

    private AddressMapper addressMapper;


    public List<AddressDto> getAddressList() {

        return addressRepository.findAll().stream().map(address ->
                addressMapper.toDto(address)).collect(Collectors.toList());

    }

    public AddressDto registerCountry(CountryCommand command) {

        if (command.getCountry() == Country.IE) {
            IrishAddress address = new IrishAddress();
            address.setCountry(command.getCountry());
            addressRepository.save(address);
            return addressMapper.toDto(address);

        } else {
            AddressWithPostalCode address = new AddressWithPostalCode();
            address.setCountry(command.getCountry());
            addressRepository.save(address);
            return addressMapper.toDto(address);
        }
    }



}
