package com.example.restpost.controller;

import com.example.restpost.dtos.shipment_commands.CreateEmptyCommand;
import com.example.restpost.dtos.shipment_commands.UpdateShipmentCommand;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.service.ShipmentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private ShipmentService shipmentService;

    @PostMapping
    public ShipmentDto createEmptyShipment() {
        return shipmentService.createEmptyShipment();
    }

    @PutMapping
    public ShipmentDto updateShipment(@Valid @RequestBody UpdateShipmentCommand command) {
        return shipmentService.updateShipment(command);
    }
}
