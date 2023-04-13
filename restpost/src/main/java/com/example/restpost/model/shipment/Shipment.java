package com.example.restpost.model.shipment;


import com.example.restpost.model.address.Address;
import com.example.restpost.model.packages.Package;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @ManyToOne
 //   @JoinColumn(name = "address_from")
    private Address from;

    @ManyToOne
//    @JoinColumn(name = "address_to")
    private Address to;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @OneToMany(mappedBy = "shipment")
    private Set<Package> packages;


    public void addPackage(Package aPackage) {
        this.packages.add(aPackage);
        aPackage.setShipment(this);
    }

    public void removePackage(Package aPackage) {

        if (aPackage.getShipment() == this) {
           aPackage.setShipment(null);
           this.getPackages().remove(aPackage);
        }



    }

}
