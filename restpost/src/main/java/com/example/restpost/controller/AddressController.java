package com.example.restpost.controller;

import com.example.restpost.dtos.*;
import com.example.restpost.service.AddressService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private AddressService addressService;
    @GetMapping
    public List<AddressDto> getAddressList() {
        return addressService.getAddressList();
    }

    @PostMapping
    public AddressDto registerCountry(@RequestBody CountryCommand command) {
        return addressService.registerCountry(command);
    }

    @PutMapping("/{id}/non-ie")
    public AddressDto updateAddressWithPostalCode(@PathVariable("id") Long id, @RequestBody UpdatePostalCommand updateCommand) {

        return addressService.updateAddress(id, updateCommand);
    }

    @PutMapping("/{id}/ie")
    public AddressDto updateIrishAddress(@PathVariable("id") Long id, @RequestBody IrishUpdateCommand updateCommand) {

        return addressService.updateAddress(id, updateCommand);
    }


}
