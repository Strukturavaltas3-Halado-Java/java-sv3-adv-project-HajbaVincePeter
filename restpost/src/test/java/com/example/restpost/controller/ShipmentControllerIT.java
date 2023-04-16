package com.example.restpost.controller;

import com.example.restpost.dtos.address_commands.UpdateIrishCommand;
import com.example.restpost.dtos.address_commands.UpdatePostalCommand;
import com.example.restpost.dtos.address_dtos.AddressIrishDto;
import com.example.restpost.dtos.address_dtos.AddressWithPostalCodeDto;
import com.example.restpost.dtos.package_commands.CreatePackageCommand;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.dtos.shipment_commands.UpdateShipmentCommand;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;
import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.County;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;
import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ShipmentControllerIT {


    @Autowired
    WebTestClient webTestClient;

    @Test
    void createShipmentTest() {

        webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShipmentDto.class)
                .value(shipmentDto -> assertNotNull(shipmentDto.getId()))
                .value(shipmentDto -> org.junit.jupiter.api.Assertions.assertNull(shipmentDto.getFrom()))
                .value(shipmentDto -> org.junit.jupiter.api.Assertions.assertNull(shipmentDto.getTo()))
                .value(shipmentDto -> org.junit.jupiter.api.Assertions.assertNull(shipmentDto.getShippingDate()));

    }

    @Test
    void updateShipmentTest() {

        Long shipmentId = webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();


        String body = "{ \"country\":\"HU\" }";

        Long addressId = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectBody(AddressWithPostalCodeDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/non-ie").build(addressId))
                .bodyValue(new UpdatePostalCommand(Country.HU, "Valamilyen utca 6.", "Teleki Blanka", "Budapest", "1126"))
                .exchange();

        String body1 = "{ \"country\":\"IE\" }";

        Long addressId1 = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body1))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(addressId1))
                .bodyValue(new UpdateIrishCommand("Main street 6.", "G.B Show", "Dublin", County.Dublin))
                .exchange();


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long packageId = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        command.setWeight(60);

        Long packageId1 = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        UpdateShipmentCommand shipmentCommand = new UpdateShipmentCommand();

        shipmentCommand.setId(shipmentId);
        shipmentCommand.setFromId(addressId);
        shipmentCommand.setToId(addressId1);
        shipmentCommand.setShippingDate(LocalDate.now().plusDays(1));
        shipmentCommand.getPackagesIdList().add(packageId);
        shipmentCommand.getPackagesIdList().add(packageId1);

        Object result = webTestClient.put()
                .uri("api/shipments")
                .bodyValue(shipmentCommand)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(Object.class)
                .returnResult().getResponseBody();

        assertAll(() -> assertTrue(result.toString().contains(String.format("{id=%s, trackingNumber=null, " +
                        "from={id=%s, country=HU, streetAddress=Valamilyen utca 6., name=Teleki Blanka, city=Budapest, postalCode=1126}, " +
                        "to={id=%s, country=IE, streetAddress=Main street 6., name=G.B Show, city=Dublin, county=Dublin}, " +
                        "shippingDate=%s", shipmentId, addressId, addressId1, LocalDate.now().plusDays(1)))),
                () -> assertTrue(result.toString().contains(String.format("{id=%s, weight=50, shipmentId=%s}", packageId, shipmentId))),
                () -> assertTrue(result.toString().contains(String.format("{id=%s, weight=60, shipmentId=%s}", packageId1, shipmentId))));


    }

    @Test
    void notFutureDateUpdateTest() {

        Long shipmentId = webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();


        UpdateShipmentCommand shipmentCommand = new UpdateShipmentCommand();
        shipmentCommand.setId(shipmentId);
        shipmentCommand.setShippingDate(LocalDate.now().minusDays(1));

        webTestClient.put()
                .uri("api/shipments")
                .bodyValue(shipmentCommand)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains(String.format("Field error in object 'updateShipmentCommand' on field 'shippingDate': " +
                                "rejected value [%s]; codes " +
                                "[Future.updateShipmentCommand.shippingDate,Future.shippingDate,Future.java.time.LocalDate,Future]; " +
                                "arguments [org.springframework.context.support.DefaultMessageSourceResolvable: " +
                                "codes [updateShipmentCommand.shippingDate,shippingDate]; arguments []; default message [shippingDate]", LocalDate.now().minusDays(1)))));


    }


    @Test
    void addPackageToShipmentTest() {

        Long shipmentId = webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();

        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long packageId = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{shipmentId}/package/{packageId}").build(shipmentId, packageId))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(ShipmentDto.class)
                .value(shipmentDto -> shipmentDto.getPackages().stream().forEach(packageDto -> assertEquals(packageId, packageDto.getId())))
                .value(shipmentDto -> assertEquals(shipmentId, shipmentDto.getId()));

    }

    @Nested
    @Sql(scripts = {"/cleartables.sql"})
    class GetAndDeleteTests {
        Long shipmentId;
        Long packageId;

        @BeforeEach
        void init() {

            shipmentId = webTestClient.post()
                    .uri("/api/shipments")
                    .exchange()
                    .expectBody(ShipmentDto.class)
                    .returnResult().getResponseBody().getId();
            CreatePackageCommand command = new CreatePackageCommand();
            command.setWeight(50);
            packageId = webTestClient.post()
                    .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(command)
                    .exchange()
                    .expectBody(PackageDto.class)
                    .returnResult().getResponseBody().getId();


        }


        @Test
        void getShipmentTest() {


            webTestClient.get()
                    .uri("/api/shipments")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(ShipmentDto.class).hasSize(1)
                    .value(l -> Assertions.assertThat(l).extracting(ShipmentDto::getId).isEqualTo(List.of(shipmentId)))
                    .value(l -> Assertions.assertThat(l).extracting(ShipmentDto::getFrom).isEqualTo(Arrays.asList(new Object[1])))
                    .value(l -> Assertions.assertThat(l).extracting(ShipmentDto::getTo).isEqualTo(Arrays.asList(new Object[1])))
                    .value(l -> Assertions.assertThat(l).extracting(shipmentDto -> shipmentDto.getPackages().size()).contains(0))
                    .value(l -> Assertions.assertThat(l).extracting(ShipmentDto::getShippingDate).isEqualTo(Arrays.asList(new Object[1])));

        }


        @Test
        void addNotExistingPackageToShipmentTest() {


            webTestClient.put()
                    .uri(uriBuilder -> uriBuilder.path("api/shipments/{shipmentId}/package/{packageId}").build(shipmentId, packageId + 1))
                    .exchange()
                    .expectBody(ProblemDetail.class)
                    .value(problemDetail -> assertEquals(404, problemDetail.getStatus()))
                    .value(problemDetail -> assertEquals(URI.create("packages/not-found"), problemDetail.getType()))
                    .value(problemDetail -> assertTrue(problemDetail.getDetail().contains(String.format("There is no package with the id: %s.", packageId + 1))));

        }

        @Test
        void addPackageToNotExistingShipment() {
            webTestClient.put()
                    .uri(uriBuilder -> uriBuilder.path("api/shipments/{shipmentId}/package/{packageId}").build(shipmentId + 1, packageId))
                    .exchange()
                    .expectBody(ProblemDetail.class)
                    .value(problemDetail -> assertEquals(404, problemDetail.getStatus()))
                    .value(problemDetail -> assertEquals(URI.create("shipments/not-found"), problemDetail.getType()))
                    .value(problemDetail -> assertTrue(problemDetail.getDetail().contains(String.format("There is no shipment with the id: %s.", shipmentId + 1))));

        }

        @Test
        void deleteNotExistingShipment() {
            webTestClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}").build(shipmentId+1))
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(ProblemDetail.class)
                    .value(problemDetail -> assertEquals(URI.create("shipments/not-found"),problemDetail.getType()))
                    .value(problemDetail -> assertTrue(problemDetail.getDetail().contains(String.format("There is no shipment with the id: %s.", shipmentId + 1))));

        }


    }

    @Test
    void processAndTrackingShipmentTest() {

        Long shipmentId = webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();


        String body = "{ \"country\":\"HU\" }";

        Long addressId = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectBody(AddressWithPostalCodeDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/non-ie").build(addressId))
                .bodyValue(new UpdatePostalCommand(Country.HU, "Valamilyen utca 6.", "Teleki Blanka", "Budapest", "1126"))
                .exchange();

        String body1 = "{ \"country\":\"IE\" }";

        Long addressId1 = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body1))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(addressId1))
                .bodyValue(new UpdateIrishCommand("Main street 6.", "G.B Show", "Dublin", County.Dublin))
                .exchange();


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long packageId = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        command.setWeight(60);

        Long packageId1 = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        UpdateShipmentCommand shipmentCommand = new UpdateShipmentCommand();

        shipmentCommand.setId(shipmentId);
        shipmentCommand.setFromId(addressId);
        shipmentCommand.setToId(addressId1);
        shipmentCommand.setShippingDate(LocalDate.now().plusDays(1));
        shipmentCommand.getPackagesIdList().add(packageId);
        shipmentCommand.getPackagesIdList().add(packageId1);

        webTestClient.put()
                .uri("api/shipments")
                .bodyValue(shipmentCommand)
                .exchange();


        ShipmentDto result = webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}/process").build(shipmentId))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody();

        String tracking = result.getTrackingNumber();

        assertNotNull(tracking);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}").build(tracking))
                .exchange()
                .expectBody(ShipmentDto.class)
                .value(shipmentDto -> assertEquals(LocalDate.now().plusDays(1), shipmentDto.getShippingDate()))
                .value(shipmentDto -> assertEquals(tracking, shipmentDto.getTrackingNumber()));

        Object result1 = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}").build(tracking))
                .exchange()
                .expectBody(Object.class)
                .returnResult().getResponseBody();


        assertAll(() -> assertTrue(result1.toString().contains(String.format("{id=%s, trackingNumber=%s, " +
                        "from={id=%s, country=HU, streetAddress=Valamilyen utca 6., name=Teleki Blanka, city=Budapest, postalCode=1126}, " +
                        "to={id=%s, country=IE, streetAddress=Main street 6., name=G.B Show, city=Dublin, county=Dublin}, " +
                        "shippingDate=%s", shipmentId, tracking, addressId, addressId1, LocalDate.now().plusDays(1)))),
                () -> assertTrue(result1.toString().contains(String.format("{id=%s, weight=50, shipmentId=%s}", packageId, shipmentId))),
                () -> assertTrue(result1.toString().contains(String.format("{id=%s, weight=60, shipmentId=%s}", packageId1, shipmentId))));


    }

    @Test
    void shipmentNotReadyTest() {

        Long shipmentId = webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();


        String body = "{ \"country\":\"HU\" }";

        Long addressId = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectBody(AddressWithPostalCodeDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/non-ie").build(addressId))
                .bodyValue(new UpdatePostalCommand(Country.HU, "Valamilyen utca 6.", "Teleki Blanka", "Budapest", "1126"))
                .exchange();

        String body1 = "{ \"country\":\"IE\" }";

        Long addressId1 = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body1))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long packageId = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        command.setWeight(60);

        Long packageId1 = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        UpdateShipmentCommand shipmentCommand = new UpdateShipmentCommand();

        shipmentCommand.setId(shipmentId);
        shipmentCommand.setFromId(addressId);
        shipmentCommand.setToId(addressId1);
        shipmentCommand.setShippingDate(LocalDate.now().plusDays(1));
        shipmentCommand.getPackagesIdList().add(packageId);
        shipmentCommand.getPackagesIdList().add(packageId1);

        webTestClient.put()
                .uri("api/shipments")
                .bodyValue(shipmentCommand)
                .exchange();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}/process").build(shipmentId))
                .exchange()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("shipments/not-complete"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains(
                        String.format("Please complete the shipment details first for the shipment with id: %s!", shipmentId))));


    }

    @Test
    void toOldShipmentToProcessTest() {

        Long shipmentId = webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();


        String body = "{ \"country\":\"HU\" }";

        Long addressId = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectBody(AddressWithPostalCodeDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/non-ie").build(addressId))
                .bodyValue(new UpdatePostalCommand(Country.HU, "Valamilyen utca 6.", "Teleki Blanka", "Budapest", "1126"))
                .exchange();

        String body1 = "{ \"country\":\"IE\" }";

        Long addressId1 = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body1))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(addressId1))
                .bodyValue(new UpdateIrishCommand("Main street 6.", "G.B Show", "Dublin", County.Dublin))
                .exchange();


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long packageId = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        command.setWeight(60);

        Long packageId1 = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        UpdateShipmentCommand shipmentCommand = new UpdateShipmentCommand();

        shipmentCommand.setId(shipmentId);
        shipmentCommand.setFromId(addressId);
        shipmentCommand.setToId(addressId1);
        shipmentCommand.setShippingDate(LocalDate.now());
        shipmentCommand.getPackagesIdList().add(packageId);
        shipmentCommand.getPackagesIdList().add(packageId1);

        webTestClient.put()
                .uri("api/shipments")
                .bodyValue(shipmentCommand)
                .exchange();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}/process").build(shipmentId))
                .exchange()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("shipments/not-complete"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains(
                        String.format("Please complete the shipment details first for the shipment with id: %s!", shipmentId))));

    }

    @Test
    void deleteTest() {
        Long shipmentId = webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();


        String body = "{ \"country\":\"HU\" }";

        Long addressId = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectBody(AddressWithPostalCodeDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/non-ie").build(addressId))
                .bodyValue(new UpdatePostalCommand(Country.HU, "Valamilyen utca 6.", "Teleki Blanka", "Budapest", "1126"))
                .exchange();

        String body1 = "{ \"country\":\"IE\" }";

        Long addressId1 = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body1))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(addressId1))
                .bodyValue(new UpdateIrishCommand("Main street 6.", "G.B Show", "Dublin", County.Dublin))
                .exchange();


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long packageId = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        command.setWeight(60);

        Long packageId1 = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange().expectBody(PackageDto.class).returnResult().getResponseBody().getId();

        UpdateShipmentCommand shipmentCommand = new UpdateShipmentCommand();

        shipmentCommand.setId(shipmentId);
        shipmentCommand.setFromId(addressId);
        shipmentCommand.setToId(addressId1);
        shipmentCommand.setShippingDate(LocalDate.now().plusDays(1));
        shipmentCommand.getPackagesIdList().add(packageId);
        shipmentCommand.getPackagesIdList().add(packageId1);

        webTestClient.put()
                .uri("api/shipments")
                .bodyValue(shipmentCommand)
                .exchange();


        String trackingNumber = webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}/process").build(shipmentId))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getTrackingNumber();

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}").build(shipmentId))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(ShipmentDto.class)
                .value(shipmentDto -> assertEquals(trackingNumber, shipmentDto.getTrackingNumber()));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{id}").build(trackingNumber))
                .exchange()
                .expectStatus().isNotFound();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/packages/{id}").build(packageId1))
                .exchange()
                .expectBody(PackageDto.class)
                .value(packageDto -> assertNull(packageDto.getShipmentId()));


    }


}













