package com.example.restpost.dtos;

import com.example.restpost.dtos.address_dtos.AddressIrishDto;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.County;
import com.example.restpost.model.shipment.Shipment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyzerTest {


    Analyzer analyzer = new Analyzer();

    @Test
    void containsNullTest() throws IllegalAccessException {


        ShipmentDto empty = new ShipmentDto();

        ShipmentDto withDate = new ShipmentDto();
        withDate.setShippingDate(LocalDate.now());

        ShipmentDto withAddresses = new ShipmentDto();
        withAddresses.setShippingDate(LocalDate.now());
        AddressIrishDto addressIrishDto = new AddressIrishDto();
        withAddresses.setFrom(addressIrishDto);
        withAddresses.setTo(addressIrishDto);

        ShipmentDto emptyWithPackagesAddresses = new ShipmentDto();
        emptyWithPackagesAddresses.setShippingDate(LocalDate.now());
        emptyWithPackagesAddresses.setFrom(addressIrishDto);
        emptyWithPackagesAddresses.setTo(addressIrishDto);
        PackageDto packageDto = new PackageDto();
        emptyWithPackagesAddresses.getPackages().add(packageDto);

        ShipmentDto ready = new ShipmentDto();
        ready.setId(1L);
        ready.setTrackingNumber("number");
        ready.setShippingDate(LocalDate.now());

        addressIrishDto.setId(1L);
        addressIrishDto.setName("test");
        addressIrishDto.setStreetAddress("test");
        addressIrishDto.setCity("test");
        addressIrishDto.setCounty(County.Dublin);
        addressIrishDto.setCountry(Country.IE);

        ready.setFrom(addressIrishDto);
        ready.setTo(addressIrishDto);

        packageDto.setWeight(20);
        packageDto.setId(1L);
        packageDto.setShipmentId(1L);

        ready.getPackages().add(packageDto);

        assertAll(
                () -> assertTrue(analyzer.containsNull(empty)),
                () -> assertTrue(analyzer.containsNull(withDate)),
                () -> assertTrue(analyzer.containsNull(withAddresses)),
                () -> assertTrue(analyzer.containsNull(emptyWithPackagesAddresses)),
                () -> assertFalse(analyzer.containsNull(ready)),


                ()-> { ready.getPackages().stream().forEach(packageDto1 ->packageDto1.setWeight(null));
                    assertTrue(analyzer.containsNull(ready));});

    }


}
