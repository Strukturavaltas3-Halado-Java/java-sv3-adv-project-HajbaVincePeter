package com.example.restpost.controller;

import com.example.restpost.dtos.package_commands.CreatePackageCommand;
import com.example.restpost.dtos.package_commands.UpdatePackageCommand;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.service.PackageService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/packages")
public class PackageController {


    private PackageService packageService;

    @GetMapping
    public List<PackageDto> getPackages() {
        return packageService.getPackages();
    }

    @GetMapping("/shipment/{shipmentId}/")
    public List<PackageDto> getShipmentPackages(@PathVariable long shipmentId) {
        return packageService.getShipmentPackages(shipmentId);
    }
    @GetMapping("/{id}")
    public PackageDto getPackageById(@PathVariable long id) {
        return packageService.getPackageById(id);
    }

    @PostMapping
    public PackageDto createPackage(@Valid @RequestBody CreatePackageCommand command) {
        return packageService.createPackage(command);
    }

    @PutMapping
    public PackageDto updatePackageWeight(@RequestBody @Valid UpdatePackageCommand command) {
        return packageService.updatePackageWeight(command);
    }
}
