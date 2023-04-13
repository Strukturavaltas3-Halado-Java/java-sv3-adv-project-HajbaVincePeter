package com.example.restpost.controller;

import com.example.restpost.dtos.shipment_commands.CreateEmptyCommand;
import com.example.restpost.dtos.shipment_commands.UpdateShipmentCommand;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.service.ShipmentService;
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
    public ShipmentDto addPackageToShipment(@PathVariable long shipmentId, @PathVariable long packageId) {
        return shipmentService.addPackageToShipment(shipmentId, packageId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShipmentDto> getShipments() {
        return shipmentService.getShipments();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ShipmentDto deleteShipment(@PathVariable long id) {
        return shipmentService.deleteShipment(id);
    }
}
