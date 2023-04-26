package com.example.restpost.controller;


import com.example.restpost.dtos.shipment_commands.UpdateShipmentCommand;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/shipments")
@Tag(name = "Shipment operations", description = "Creating, updating, finding and deleting shipments.")
public class ShipmentController {

    private ShipmentService shipmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Creating an empty shipment.", summary = "CREATE SHIPMENT")
    public ShipmentDto createEmptyShipment() {
        return shipmentService.createEmptyShipment();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Updating a shipment.",summary = "UPDATE SHIPMENT")
    public ShipmentDto updateShipment(@Valid @RequestBody UpdateShipmentCommand command) {
        return shipmentService.updateShipment(command);
    }

    @PutMapping("/{shipmentId}/package/{packageId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Adding a package to a shipment.", summary = "ADD A PACKAGE")
    public ShipmentDto addPackageToShipment(@PathVariable long shipmentId, @PathVariable long packageId) {
        return shipmentService.addPackageToShipment(shipmentId, packageId);
    }


    @PutMapping("/{id}/process")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(description = "Processing a shipment", summary = "PROCESS SHIPMENT")
    public ShipmentDto processShipment(@PathVariable long id) throws IllegalAccessException {
        return shipmentService.processShipment(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Listing all the shipments.", summary = "GET SHIPMENT LIST")
    public List<ShipmentDto> getShipments() {
        return shipmentService.getShipments();
    }

    @GetMapping("/{trackingnumber}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Finding a processed shipment by the tracking number.", summary = "TRACK SHIPMENT")
    public ShipmentDto trackShipment(@PathVariable("trackingnumber") String trackingNumber) {
        return shipmentService.trackShipment(trackingNumber);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Deleting a shipment",operationId = "shipment id", summary = "DELETE SHIPMENT")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ShipmentDto deleteShipment(@PathVariable long id) {
        return shipmentService.deleteShipment(id);
    }

}
