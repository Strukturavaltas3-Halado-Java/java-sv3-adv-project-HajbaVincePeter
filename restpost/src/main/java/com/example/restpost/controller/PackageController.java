package com.example.restpost.controller;

import com.example.restpost.dtos.package_commands.CreatePackageCommand;
import com.example.restpost.dtos.package_commands.UpdatePackageCommand;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/packages")
@Tag(name = "Package operations", description = "Creating, updating find and deleting packages")
public class PackageController {


    private PackageService packageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Creating a new package with it's wait.")
    public PackageDto createPackage(@Valid @RequestBody CreatePackageCommand command) {
        return packageService.createPackage(command);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Updating the wight of a package identified by it's own id and the id of it's shipment.")
    public PackageDto updatePackageWeight(@RequestBody @Valid UpdatePackageCommand command) {
        return packageService.updatePackageWeight(command);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Listing all the packages.")
    public List<PackageDto> getPackages() {
        return packageService.getPackages();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Searching for a shipment by id.")
    public PackageDto getPackageById(@PathVariable long id) {
        return packageService.getPackageById(id);
    }

    @GetMapping("/shipment/{shipmentId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Listing the packages of a given shipment.")
    public List<PackageDto> getShipmentPackages(@PathVariable long shipmentId) {
        return packageService.getShipmentPackages(shipmentId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Deleting a shipment")
    public PackageDto deletePackage(@PathVariable long id) {
        return packageService.deletePackage(id);
    }
}
