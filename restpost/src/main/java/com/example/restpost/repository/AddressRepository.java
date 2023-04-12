package com.example.restpost.repository;

import com.example.restpost.model.address.Address;
import com.example.restpost.model.address.AddressIrish;
import com.example.restpost.model.address.AddressWithPostalCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    AddressIrish findIrishAddressById(Long id);
    AddressWithPostalCode findAddressWithPostalCodeById(Long id);



}
