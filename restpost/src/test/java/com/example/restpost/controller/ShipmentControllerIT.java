package com.example.restpost.controller;

import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Sql(scripts = {"/cleartables.sql"})
public class ShipmentControllerIT {

    @Autowired
    WebTestClient webTestClient;
    @Test
    @Order(1)
    void createShipmentTest() {

     webTestClient.post()
             .uri("/api/shipments")
             .exchange()
             .expectStatus().isCreated()
             .expectBody(ShipmentDto.class)
             .value(shipmentDto -> org.junit.jupiter.api.Assertions.assertNotNull(shipmentDto.getId()))
             .value(shipmentDto -> org.junit.jupiter.api.Assertions.assertNull(shipmentDto.getFrom()))
             .value(shipmentDto -> org.junit.jupiter.api.Assertions.assertNull(shipmentDto.getTo()))
             .value(shipmentDto -> org.junit.jupiter.api.Assertions.assertNull(shipmentDto.getShippingDate()));

    }

    @Test
    @Order(2)
    void getShipmentTest(){

    ShipmentDto expected = new ShipmentDto();
                expected.setId(1L);

        webTestClient.get()
                .uri("/api/shipments")
                .exchange()
                .expectStatus().isFound()
                .expectBodyList(ShipmentDto.class).hasSize(1)
                .value(l-> Assertions.assertThat(l).extracting(ShipmentDto::getId).isEqualTo(List.of(1L)))
                .value(l-> Assertions.assertThat(l).extracting(ShipmentDto::getFrom).isEqualTo(Arrays.asList(new Object[1])))
                .value(l-> Assertions.assertThat(l).extracting(ShipmentDto::getTo).isEqualTo(Arrays.asList(new Object[1])))
                .value(l-> Assertions.assertThat(l).extracting(shipmentDto -> shipmentDto.getPackages().size()).contains(0))
                .value(l-> Assertions.assertThat(l).extracting(ShipmentDto::getShippingDate).isEqualTo(Arrays.asList(new Object[1])));



    }




}
