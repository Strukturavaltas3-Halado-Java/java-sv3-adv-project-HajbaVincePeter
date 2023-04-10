package com.example.restpost.service;

import com.example.restpost.dtos.*;
import com.example.restpost.exception.NoAddressWithIdException;
import com.example.restpost.mapper.AddressMapper;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.address.AddressWithPostalCode;
import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.IrishAddress;
import com.example.restpost.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressRepository addressRepository;

    private AddressMapper addressMapper;


    public List<AddressDto> getAddressList() {

        return addressRepository.findAll().stream().map(address ->
        { if(address.getCountry()==Country.IE){
            return addressMapper.toDto((IrishAddress) addressRepository.findIrishAddressById(address.getId()));
        }  else {
            return addressMapper.toDto( (AddressWithPostalCode) addressRepository.findAddressWithPostalCodeById(address.getId()));}})
                .collect(Collectors.toList());
    }

    @Transactional
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

    @Transactional
    public AddressDto updateAddress(Long id, UpdateCommand updateCommand) {

        Optional<Address> address = addressRepository.findById(id);
        
        if( address.isPresent()) {
            address.get().setName(updateCommand.getName());
            address.get().setStreetAddress(updateCommand.getStreetAddress());
            if (address.get() instanceof AddressWithPostalCode) {
                ((AddressWithPostalCode) address.get()).setPostalCode(((UpdatePostalCommand) updateCommand).getPostalCode());

                return addressMapper.toDto((AddressWithPostalCode) address.get());
            } else {
                ((IrishAddress) address.get()).setCounty(((IrishUpdateCommand) updateCommand).getCounty());

                return addressMapper.toDto((IrishAddress) address.get());
            }
        }
        throw new NoAddressWithIdException(id);
    }


}
