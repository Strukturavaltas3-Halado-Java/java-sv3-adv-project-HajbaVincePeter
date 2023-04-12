package com.example.restpost.service;

import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.dtos.shipment_commands.UpdateShipmentCommand;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.exception.NoAddressWithIdException;
import com.example.restpost.exception.NoPackageWithIdException;
import com.example.restpost.exception.NoShipmentWithIdExtension;
import com.example.restpost.mapper.ShipmentMapper;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.packages.Package;
import com.example.restpost.model.shipment.Shipment;
import com.example.restpost.repository.AddressRepository;
import com.example.restpost.repository.PackageRepository;
import com.example.restpost.repository.ShipmentRepository;
import jakarta.persistence.Access;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ShipmentService {

    private ShipmentRepository shipmentRepository;

    private PackageRepository packageRepository;

    private AddressRepository addressRepository;

    private ShipmentMapper shipmentMapper;


    @Transactional
    public ShipmentDto createEmptyShipment() {
        Shipment shipment = new Shipment();
        shipmentRepository.save(shipment);
        return shipmentMapper.toDto(shipment);
    }


    @Transactional
    public ShipmentDto updateShipment(UpdateShipmentCommand command) {

        Shipment shipment = shipmentRepository.findById(command.getId()).orElseThrow(() ->
                new NoShipmentWithIdExtension(command.getId())
        );

        shipment.setShippingDate(command.getShippingDate());

//        Optional<Address> fromOptional = addressRepository.findById(command.getFromId());
//        Optional<Address> toOptional = addressRepository.findById(command.getToId());
//
//        if (fromOptional.isPresent()) {
//            shipment.setFrom(fromOptional.get());
//        }
//        if (toOptional.isPresent()) {
//            shipment.setTo(toOptional.get()); }

        if (command.getFromId() != null) {
            Address from = addressRepository.findById(command.getFromId())
                    .orElseThrow(() -> new NoAddressWithIdException(command.getFromId()));
            shipment.setFrom(from);
        }
        if (command.getToId() != null) {
            Address to = addressRepository.findById(command.getToId())
                    .orElseThrow(() -> new NoAddressWithIdException(command.getToId()));
            shipment.setTo(to);
        }

        shipment.setPackages(command.getPackagesIdList().stream().map(packageId ->
                {
                    Package aPackage = packageRepository.findById(packageId).orElseThrow(
                            () -> new NoPackageWithIdException(packageId));
                    shipment.addPackage(aPackage);
                    return aPackage;
                })
                .collect(Collectors.toSet()));


        return shipmentMapper.toDto(shipment);


    }

    ;


}