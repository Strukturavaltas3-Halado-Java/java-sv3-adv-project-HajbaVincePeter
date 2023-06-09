package com.example.restpost.controller;

import com.example.restpost.dtos.address_commands.CountryCommand;
import com.example.restpost.dtos.address_commands.UpdateIrishCommand;
import com.example.restpost.dtos.address_commands.UpdatePostalCommand;
import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.County;
import com.example.restpost.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Address operations", description = "Saving, modifying, finding and deleting addresses." )
public class AddressController {


    private AddressService addressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Creating an empty address by defining the country.",summary = "CREATE ADDRESS")
    public AddressDto registerCountry(@Valid @RequestBody CountryCommand command) {
        return addressService.registerCountry(command);
    }

    @PutMapping("/{id}/non-ie")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Updating an address in a country with postal code",summary = "UPDATE POSTAL CODE ADDRESS")
    public AddressDto updateAddressWithPostalCode( @PathVariable("id")  Long id, @Valid @RequestBody UpdatePostalCommand updateCommand) {

        return addressService.updateAddress(id, updateCommand);
    }

    @PutMapping("/{id}/ie")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Updating an Irish address.",summary = "UPDATE IRISH ADDRESS")
    public AddressDto updateIrishAddress(@PathVariable("id") Long id, @Valid @RequestBody UpdateIrishCommand updateCommand) {

        return addressService.updateAddress(id, updateCommand);
    }

    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Extracting the  country enum data.", summary = "COUNTRY DATA")
    public Country.CountryData getCountries(){
        return new Country.CountryData();
    }

    @GetMapping("/counties")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Extracting the county enum data.", summary = "COUNTY DATA")
    public List<County> getCounties(){

        return List.of(County.values());
    }



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Listing all the saved addresses.",summary = "GET ADDRESS LIST")
    public List<AddressDto> getAddressList() {
        return addressService.getAddressList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Searching for an address by id.", summary = "FIND ADDRESS")
    public AddressDto getAddressById(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }

    @DeleteMapping("/{id}")@ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Deleting an address.", summary = "DELETE ADDRESS")
    public AddressDto deleteAddress(@PathVariable("id") long id) {
        return addressService.deleteAddress(id);
    }



}
