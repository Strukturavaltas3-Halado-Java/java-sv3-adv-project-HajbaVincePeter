package com.example.restpost.repository;

import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.mapper.AddressMapper;
import com.example.restpost.model.address.*;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    AddressRepository addressRepository;



    @Autowired
    AddressMapper addressMapper;


    @Test
    void polymorphism(){

        Address address = new Address();
        address.setName("XY");
        address.setCountry(Country.HU);
        address.setStreetAddress("utca");

        addressRepository.save(address);

        Address address1 = new AddressWithPostalCode();
        address1.setName("XY");
        address1.setCountry(Country.HU);
        address1.setStreetAddress("utca");
        ((AddressWithPostalCode) address1).setPostalCode("1126");

        addressRepository.save(address1);

        Address address2 = new AddressIrish();
        address2.setName("XY");
        address2.setCountry(Country.IE);
        address2.setStreetAddress("utca");
        ((AddressIrish)address2).setCounty(County.Limerick);

        addressRepository.save(address2);

        List<Address> addresses = addressRepository.findAll();
        System.out.println(addresses);
        org.assertj.core.api.Assertions.assertThat(addresses)
                .hasSize(3);



        assertEquals("1126",((AddressWithPostalCode) addresses.get(2)).getPostalCode());

        assertTrue(addresses.get(2) instanceof AddressWithPostalCode);
        assertTrue(addresses.get(1) instanceof AddressIrish);

        System.out.println(address2.getClass().getSimpleName());


      AddressDto addressDto= addressMapper.toDto((AddressIrish) address2);
        System.out.println(addressDto.toString());


    }










}
