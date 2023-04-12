package com.example.restpost.repository;

import com.example.restpost.model.packages.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package,Long> {



    @Query("select p from Package p where p.shipment.id = :shipmentId")
    List<Package> findShipmentPackages(@Param("shipmentId") Long shipmentId );

}
