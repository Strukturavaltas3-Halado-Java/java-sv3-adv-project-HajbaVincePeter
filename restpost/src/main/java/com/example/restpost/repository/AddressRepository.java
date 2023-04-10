package com.example.restpost.repository;

import com.example.restpost.model.address.Address;
import com.example.restpost.model.address.AddressWithPostalCode;
import com.example.restpost.model.address.IrishAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    IrishAddress findIrishAddressById(Long id);
    AddressWithPostalCode findAddressWithPostalCodeById(Long id);


}
