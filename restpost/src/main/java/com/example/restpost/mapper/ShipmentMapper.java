package com.example.restpost.mapper;

import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.dtos.address_dtos.AddressIrishDto;
import com.example.restpost.dtos.address_dtos.AddressWithPostalCodeDto;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.address.AddressIrish;
import com.example.restpost.model.address.AddressWithPostalCode;
import com.example.restpost.model.packages.Package;
import com.example.restpost.model.shipment.Shipment;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface ShipmentMapper {

     ShipmentDto toDto(Shipment shipment);

default      AddressDto map(Address value){
     if (value instanceof AddressIrish) {
           return map((AddressIrish) value);
     }else {
           return map((AddressWithPostalCode) value);

     }

};

     AddressIrishDto map(AddressIrish addressIrish);

     AddressWithPostalCodeDto map(AddressWithPostalCode addressWithPostalCode);

     default PackageDto toDto(Package box) {

          PackageDto packageDto = new PackageDto();

          packageDto.setId(box.getId());
          packageDto.setWeight(box.getWeight());
          if (box.getShipment() != null) {
               packageDto.setShipmentId(box.getShipment().getId());
          }
          return packageDto;

     }
     List<ShipmentDto> toDto(List<Shipment> shipments);

//     {
//
//        ShipmentDto shipmentDto = new ShipmentDto();
//
//       if(shipment.getPackages() != null) {
//         shipmentDto.
//
//       }
//
//    };





}
