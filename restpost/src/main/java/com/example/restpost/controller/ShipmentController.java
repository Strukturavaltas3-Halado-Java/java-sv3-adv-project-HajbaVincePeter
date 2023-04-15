package com.example.restpost.controller;

import com.example.restpost.dtos.shipment_commands.CreateEmptyCommand;
import com.example.restpost.dtos.shipment_commands.UpdateShipmentCommand;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.service.ShipmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/shipments")
@Tag(name = "Shipment operations", description = "Creating, updating, finding and deleting shipments.")
public class ShipmentController {

    private ShipmentService shipmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentDto createEmptyShipment() {
        return shipmentService.createEmptyShipment();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ShipmentDto updateShipment(@Valid @RequestBody UpdateShipmentCommand command) {
        return shipmentService.updateShipment(command);
    }

    @PutMapping("/{shipmentId}/package/{packageId}")
    @Tag(name = "ADD A PACKAGE", description = "Adding a package to a shipment")
    public ShipmentDto addPackageToShipment(@PathVariable long shipmentId, @PathVariable long packageId) {
        return shipmentService.addPackageToShipment(shipmentId, packageId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShipmentDto> getShipments() {
        return shipmentService.getShipments();
    }




    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShipmentDto trackShipment(@PathVariable("id") String trackingNumber) {
        return shipmentService.trackShipment(trackingNumber);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ShipmentDto deleteShipment(@PathVariable long id) {
        return shipmentService.deleteShipment(id);
    }


    @PutMapping("/{id}/process")
    public ShipmentDto processShipment(@PathVariable long id) throws IllegalAccessException {
        return shipmentService.processShipment(id);
    }

}
