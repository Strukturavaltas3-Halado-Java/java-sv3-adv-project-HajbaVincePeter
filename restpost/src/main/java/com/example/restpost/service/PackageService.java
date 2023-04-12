package com.example.restpost.service;

import com.example.restpost.dtos.package_commands.CreatePackageCommand;
import com.example.restpost.dtos.package_commands.UpdatePackageCommand;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.exception.NoPackageWithIdException;
import com.example.restpost.exception.PackageNotInShipmentException;
import com.example.restpost.mapper.AddressMapper;
import com.example.restpost.mapper.PackageMapper;
import com.example.restpost.model.packages.Package;
import com.example.restpost.repository.AddressRepository;
import com.example.restpost.repository.PackageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PackageService {

    private PackageRepository packageRepository;

    private PackageMapper packageMapper;


    public List<PackageDto> getPackages(){
        return  packageRepository.findAll().stream().map(aPackage ->
                packageMapper.toDto(aPackage)).collect(Collectors.toList());
    }

    public List<PackageDto> getShipmentPackages(long shipmentId) {

     return packageRepository.findShipmentPackages(shipmentId).stream().map(aPackage ->
             packageMapper.toDto(aPackage)).collect(Collectors.toList());

    }


    public PackageDto getPackageById(long id) {

      return   packageMapper.toDto(packageRepository.findById(id).orElseThrow(()->
              new NoPackageWithIdException(id)));
    }

    @Transactional
    public PackageDto createPackage(CreatePackageCommand command) {
        Package aPackage = new Package();
        aPackage.setWeight(command.getWeight());
        packageRepository.save(aPackage);
        return packageMapper.toDto(aPackage);
    }

    @Transactional
    public PackageDto updatePackageWeight(UpdatePackageCommand command) {

        Package aPackage = packageRepository.findById(command.getId()).orElseThrow(()->
                    new NoPackageWithIdException(command.getId()));
        if (command.getShipmentId()!= aPackage.getShipment().getId()){
            throw new PackageNotInShipmentException(command.getId(),command.getShipmentId());
        }
        aPackage.setWeight(command.getWeight());

        return packageMapper.toDto(aPackage);
    }









}