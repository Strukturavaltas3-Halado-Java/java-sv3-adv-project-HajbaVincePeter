package com.example.restpost.mapper;

import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.model.packages.Package;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PackageMapper {

   default PackageDto toDto(Package box){

       PackageDto packageDto = new PackageDto();

       packageDto.setId(box.getId());
       packageDto.setWeight(box.getWeight());
       if(box.getShipment() !=null){
       packageDto.setShipmentId(box.getShipment().getId());}

       return packageDto;
   }


}
