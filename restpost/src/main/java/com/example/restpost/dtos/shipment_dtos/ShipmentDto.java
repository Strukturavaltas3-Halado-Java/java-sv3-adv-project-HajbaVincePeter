package com.example.restpost.dtos.shipment_dtos;

import com.example.restpost.dtos.address_dtos.AddressDto;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.model.address.Address;
import com.example.restpost.model.packages.Package;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.val;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ShipmentDto {

    private Long id;


    private String trackingNumber;


    private AddressDto from;


    private AddressDto to;


    private LocalDate shippingDate;


    private Set<PackageDto> packages = new HashSet<>();


    public boolean notReady() throws IllegalAccessException {

        return containsNull(this);

    }

    private boolean containsNull(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return true;
        }

        Class<?> clazz = obj.getClass();


        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
         field.setAccessible(true);
            val value = field.get(obj);
            if (value == null) {
                return true;
            } else if (value.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Object arrayElement = java.lang.reflect.Array.get(value, i);
                    if (containsNull(arrayElement)) {
                        return true;
                    }
                }
            } else if (value instanceof Collection<?>) {
               if(((Collection<?>) value).size() == 0) {
                   return true;
               }

                for (Object v : ((Collection<?>) value)) {
                    {
                        try {
                            if (containsNull(v)) {
                                return true;
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }




            } else if (!field.getType().isPrimitive() && !field.getType().equals(String.class) &&
                    value.getClass().getPackage().getName().contains("com.example.restpost.dtos")) {

                if (containsNull(value)) {
                    return true;
                }
            }
        }

        return false;
    }

}
