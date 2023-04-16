package com.example.restpost.service;

import com.example.restpost.dtos.address_commands.CountryCommand;
import com.example.restpost.dtos.address_commands.UpdateIrishCommand;
import com.example.restpost.dtos.address_commands.UpdateCommand;
import com.example.restpost.dtos.address_commands.UpdatePostalCommand;
import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.exception.exceptions.AddressInShipmentException;
import com.example.restpost.exception.exceptions.CountryMismatchException;
import com.example.restpost.exception.exceptions.NoAddressWithIdException;
import com.example.restpost.mapper.AddressMapper;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.address.AddressIrish;
import com.example.restpost.model.address.AddressWithPostalCode;
import com.example.restpost.model.address.Country;
import com.example.restpost.repository.AddressRepository;
import com.example.restpost.repository.ShipmentRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressRepository addressRepository;

    private ShipmentRepository shipmentRepository;

    private AddressMapper addressMapper;


    public List<AddressDto> getAddressList() {

        return addressRepository.findAll().stream().map(address ->
                {
                    if (address instanceof AddressIrish) {
                        return addressMapper.toDto((AddressIrish) address);
                    } else {
                        return addressMapper.toDto((AddressWithPostalCode) address);
                    }
                })
                .collect(Collectors.toList());
    }

    public AddressDto getAddressById(long id) {

        Address address = addressRepository.findById(id).orElseThrow(() -> new NoAddressWithIdException(id));

        if (address instanceof AddressIrish) {
            return addressMapper.toDto((AddressIrish) address);
        } else {
            return addressMapper.toDto((AddressWithPostalCode) address);
        }
    }

    @Transactional
    public AddressDto registerCountry(CountryCommand command) {

        if (command.getCountry() == Country.IE) {
            AddressIrish address = new AddressIrish();
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
    public AddressDto updateAddress(@Valid long id, @Valid UpdateCommand updateCommand) {

        Address address = addressRepository.findById(id).orElseThrow(() -> new NoAddressWithIdException(id));

        if (address instanceof AddressWithPostalCode) {
            if (updateCommand instanceof UpdatePostalCommand && ((UpdatePostalCommand) updateCommand).getCountry()!=Country.IE) {
                updatePostal((UpdatePostalCommand) updateCommand, address);
                return addressMapper.toDto((AddressWithPostalCode) address);
            } else {
                throw new CountryMismatchException(id);
            }
        } else {
            if (updateCommand instanceof UpdateIrishCommand) {
                updateIrish((UpdateIrishCommand) updateCommand, address);
                return addressMapper.toDto((AddressIrish) address);
            } else {
                throw new CountryMismatchException(id);
            }
        }
    }

    @Transactional
    public AddressDto deleteAddress(long id) {

        Address address = addressRepository.findById(id).orElseThrow(() -> new NoAddressWithIdException(id));
        addressRepository.delete(address);

        if( shipmentRepository.findShipmentsWithAddressId(id).size()>0){
            throw new AddressInShipmentException(shipmentRepository.findShipmentsWithAddressId(id).size(),id);
        }

        if (address instanceof AddressIrish) {
            return addressMapper.toDto((AddressIrish) address);
        } else {
            return addressMapper.toDto((AddressWithPostalCode) address);
        }
    }


    private void updatePostal(UpdatePostalCommand updateCommand, Address address) {
        address.setCountry(updateCommand.getCountry());
        address.setName(updateCommand.getName());
        address.setCity(updateCommand.getCity());
        address.setStreetAddress(updateCommand.getStreetAddress());

        ((AddressWithPostalCode) address).setPostalCode(updateCommand.getPostalCode());
    }

    private void updateIrish(UpdateIrishCommand updateCommand, Address address) {
        address.setName(updateCommand.getName());
        address.setCity(updateCommand.getCity());
        address.setStreetAddress(updateCommand.getStreetAddress());

        ((AddressIrish) address).setCounty(updateCommand.getCounty());
    }
}
