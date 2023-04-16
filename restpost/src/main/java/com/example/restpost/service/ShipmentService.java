package com.example.restpost.service;

import com.example.restpost.dtos.Analyzer;
import com.example.restpost.dtos.shipment_commands.UpdateShipmentCommand;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.exception.exceptions.*;
import com.example.restpost.mapper.ShipmentMapper;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.packages.Package;
import com.example.restpost.model.shipment.Shipment;
import com.example.restpost.repository.AddressRepository;
import com.example.restpost.repository.PackageRepository;
import com.example.restpost.repository.ShipmentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.aspectj.weaver.MemberImpl.method;

@AllArgsConstructor
@Service
public class ShipmentService {

    private ShipmentRepository shipmentRepository;

    private PackageRepository packageRepository;

    private AddressRepository addressRepository;

    private ShipmentMapper shipmentMapper;

    private Analyzer analyzer;


    @Transactional
    public ShipmentDto createEmptyShipment() {
        Shipment shipment = new Shipment();
        shipmentRepository.save(shipment);
        return shipmentMapper.toDto(shipment);
    }


    @Transactional
    public ShipmentDto updateShipment(UpdateShipmentCommand command) {

        Shipment shipment = shipmentRepository.findById(command.getId()).orElseThrow(() ->
                new NoShipmentWithIdException(command.getId())
        );

        shipment.setShippingDate(command.getShippingDate());

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


    @Transactional
    public ShipmentDto processShipment(long id) throws IllegalAccessException {
        Shipment shipment = shipmentRepository.findShipment(id)
                .orElseThrow(() -> new NoShipmentWithIdException(id));
//
       if (shipment.getTrackingNumber() != null) {
           throw new ShipmentAlreadyProcessedError(id);
       }

          shipment.setTrackingNumber(String.valueOf(UUID.randomUUID()));

          ShipmentDto dto = shipmentMapper.toDto(shipment);
         if(analyzer.containsNull(dto)){
             shipment.setTrackingNumber(null);
             throw  new ShipmentNotCompleteError(id);
         };
         if(!shipment.getShippingDate().isAfter(LocalDate.now())) {
             throw new NotFutureDateException(id);
         }

         return dto;
    }

    @Transactional
    public ShipmentDto addPackageToShipment(long shipmentId, long packageId) {

        Shipment shipment = shipmentRepository.findShipment(shipmentId).orElseThrow(() ->
                new NoShipmentWithIdException(shipmentId));

        Package aPackage = packageRepository.findById(packageId).orElseThrow(() ->
                new NoPackageWithIdException(packageId));


        shipment.addPackage(aPackage);

        return shipmentMapper.toDto(shipment);
    }


    @Transactional
    public List<ShipmentDto> getShipments() {
        return shipmentMapper.toDto(shipmentRepository.findShipments());

    }

    @Transactional
    public ShipmentDto trackShipment(String trackingNumber){
         return  shipmentMapper.toDto(shipmentRepository.findByTrackingNumber(trackingNumber)
                 .orElseThrow(()-> new NoShipmentWithTrackingNumberException(trackingNumber)));

    }


    @Transactional
    public ShipmentDto deleteShipment(long id) {

        Shipment shipment = shipmentRepository.findShipment(id).orElseThrow(() ->
                new NoShipmentWithIdException(id));

        ShipmentDto deleted = shipmentMapper.toDto(shipment);

        shipment.getPackages().stream().forEach(aPackage ->
                packageRepository.delete(aPackage));
        shipmentRepository.delete(shipment);

        return deleted;

    }




}
