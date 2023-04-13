package com.example.restpost.repository;

import com.example.restpost.model.packages.Package;
import com.example.restpost.model.shipment.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {

    @Query("select distinct s from Shipment s left join fetch s.packages ")
    List<Shipment> findShipments();

    @Query("select distinct s from Shipment s where s.from.id = :id or s.to.id = :id")
    List<Shipment> findShipmentsWithAddressId(@Param("id") Long id);

    @Query("select distinct s from Shipment s left join fetch s.packages where s.id = :id")
    Optional<Shipment> findShipment(@Param("id") Long id);
    ;
}
