package com.example.restpost.controller;

import com.example.restpost.dtos.package_commands.CreatePackageCommand;
import com.example.restpost.dtos.package_commands.UpdatePackageCommand;
import com.example.restpost.dtos.package_dtos.PackageDto;
import com.example.restpost.dtos.shipment_commands.UpdateShipmentCommand;
import com.example.restpost.dtos.shipment_dtos.ShipmentDto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PackageControllerIT {

    @Autowired
    WebTestClient webTestClient;


    @Test
    void createPackageTest() {

        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PackageDto.class)
                .value(packageDto -> assertNotNull(packageDto.getId()))
                .value(packageDto -> assertNull(packageDto.getShipmentId()))
                .value(packageDto -> assertEquals(50, packageDto.getWeight()));
    }


    @Test
    void createPackageWrongWeightTest() {

        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(101);

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains("A package cannot weight more than 100 kg.")));


        command.setWeight(-10);

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains("A package has a positive weight.")));

        command.setWeight(null);

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains("The weight cannot be null.")));

    }

    @Test
    void updatePackageTest() {

        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long id = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();

        UpdatePackageCommand command1 = new UpdatePackageCommand();

        command1.setId(id);
        command1.setWeight(100);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command1)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(PackageDto.class)
                .value(packageDto -> assertEquals(id, packageDto.getId()))
                .value(packageDto -> assertEquals(100, packageDto.getWeight()))
                .value(packageDto -> assertEquals(null, packageDto.getShipmentId()));
    }

    @Test
    void wrongPackageUpdateTestNoId() {


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long id = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();

        UpdatePackageCommand command1 = new UpdatePackageCommand();


        command1.setWeight(60);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command1)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains("No package id provided.")));

    }

    @Test
    void wrongPackageUpdateTestNoWeight() {


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long id = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();

        UpdatePackageCommand command1 = new UpdatePackageCommand();


        command1.setId(id);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command1)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains("The weight cannot be null.")));

    }

    @Test
    void wrongPackageUpdateTestOverweight() {


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long id = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();

        UpdatePackageCommand command1 = new UpdatePackageCommand();


        command1.setId(id);
        command1.setWeight(101);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command1)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains("A package cannot weight more than 100 kg.")));

    }

    @Test
    void wrongPackageUpdateTestNotPositiveWeight() {


        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long id = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();

        UpdatePackageCommand command1 = new UpdatePackageCommand();


        command1.setId(id);
        command1.setWeight(0);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command1)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail().contains("A package has a positive weight.")));

    }

    @Test
    void updatePackageWithShipmentTest() {

        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long id = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();

        Long shipmentId = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/shipments").build())
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();


        UpdatePackageCommand command1 = new UpdatePackageCommand();
        UpdateShipmentCommand command2 = new UpdateShipmentCommand();
        command2.getPackagesIdList().add(id);
        command2.setId(shipmentId);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/shipments//{shipmentId}/package/{id}").build(shipmentId, id))
                .bodyValue(command2)
                .exchange();


        command1.setId(id);
        command1.setWeight(100);
        command1.setShipmentId(shipmentId);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command1)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(PackageDto.class)
                .value(packageDto -> assertEquals(id, packageDto.getId()))
                .value(packageDto -> assertEquals(100, packageDto.getWeight()))
                .value(packageDto -> assertEquals(shipmentId, packageDto.getShipmentId()));
    }

    @Test
    void updatePackageWithWrongShipmentTest() {

        CreatePackageCommand command = new CreatePackageCommand();
        command.setWeight(50);

        Long id = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();

        Long shipmentId = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/shipments").build())
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();


        UpdatePackageCommand command1 = new UpdatePackageCommand();
        UpdateShipmentCommand command2 = new UpdateShipmentCommand();
        command2.getPackagesIdList().add(id);
        command2.setId(shipmentId);

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/shipments/{shipmentId}/package/{id}").build(shipmentId, id))
                .bodyValue(command2)
                .exchange();


        command1.setId(id);
        command1.setWeight(100);
        command1.setShipmentId(shipmentId+1);

    assertAll(

 () -> webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command1)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("packages/wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains(String.format("Wrong shipment id: %s for the package with id: %s.",shipmentId+1,id)))),

    ()-> {command1.setShipmentId(null);
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command1)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("packages/wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains(String.format("Wrong shipment id: %s for the package with id: %s.",null,id))));},




            ()-> { Long id1 = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectBody(PackageDto.class)
                .returnResult().getResponseBody().getId();



        UpdatePackageCommand command3 = new UpdatePackageCommand();
        command3.setId(id1);
        command3.setWeight(30);
        command3.setShipmentId(shipmentId);


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command3)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertEquals(URI.create("packages/wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains(String.format("Wrong shipment id: %s for the package with id: %s.",shipmentId,id1))));});



    }
    @Nested
    @Sql(scripts = {"/cleartables.sql"})
    class getAndDeleteTests {

        Long id;
        Long id1;
        Long id2;
        Long shipmentId;

        @BeforeEach
        void init(){

            CreatePackageCommand command = new CreatePackageCommand();
            command.setWeight(50);

             id = webTestClient.post()
                    .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(command)
                    .exchange()
                    .expectBody(PackageDto.class)
                    .returnResult().getResponseBody().getId();

            CreatePackageCommand command1 = new CreatePackageCommand();
            command1.setWeight(60);

             id1 = webTestClient.post()
                    .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(command1)
                    .exchange()
                    .expectBody(PackageDto.class)
                    .returnResult().getResponseBody().getId();

            CreatePackageCommand command2 = new CreatePackageCommand();
            command2.setWeight(70);

             id2 = webTestClient.post()
                    .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(command2)
                    .exchange()
                    .expectBody(PackageDto.class)
                    .returnResult().getResponseBody().getId();

             shipmentId = webTestClient.post()
                    .uri(uriBuilder -> uriBuilder.path("api/shipments").build())
                    .exchange()
                    .expectBody(ShipmentDto.class)
                    .returnResult().getResponseBody().getId();


            webTestClient.put()
                    .uri(uriBuilder -> uriBuilder.path("api/shipments/{shipmentId}/package/{id}").build(shipmentId, id))
                    .exchange();



        }


        @Test
        void getAllPackagesTest() {

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("api/packages").build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(PackageDto.class)
                    .value(packageDtos -> Assertions.assertThat(packageDtos).extracting(packageDto -> packageDto.getWeight()).containsOnly(50,60,70));

        }

        @Test
        void gePackageByIdTest(){

                   webTestClient.get()
                           .uri(uriBuilder -> uriBuilder.path("api/packages/{id}").build(id))
                           .exchange()
                           .expectStatus().isOk()
                           .expectBody(PackageDto.class)
                           .value(packageDto -> assertEquals(50,packageDto.getWeight()))
                           .value(packageDto -> assertEquals(shipmentId,packageDto.getShipmentId()));

        }

        @Test
        void getPackageWithWrongIdTest(){

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("api/packages/{id}").build(id+id1+id2))
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(ProblemDetail.class)
                    .value(problemDetail -> assertEquals(URI.create("packages/not-found"),problemDetail.getType()))
                    .value(problemDetail -> assertTrue(problemDetail.getDetail().contains(String.format("There is no package with the id: %s.",id+id1+id2))));

        }

        @Test
        void getPackagesOfShipmentTest(){

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("api/packages/shipment/{shipmentId}").build(shipmentId))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(PackageDto.class)
                    .value(packageDtos -> Assertions.assertThat(packageDtos).extracting(
                            packageDto -> packageDto.getWeight()).containsOnly(50));

        }


        @Test
        void getPackagesOfShipmentEmptyListTest(){

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("api/packages/shipment/{shipmentId}").build(shipmentId+1))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(ProblemDetail.class)
                    .value(packageDtos -> Assertions.assertThat(packageDtos).isEmpty());
        }


        @Test
        void deletePackageTest(){

            webTestClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("api/packages/{id}").build(id))
                    .exchange()
                    .expectStatus().isAccepted()
                    .expectBody(PackageDto.class)
                    .value(packageDto -> assertEquals(50,packageDto.getWeight()))
                    .value(packageDto -> assertEquals(shipmentId,packageDto.getShipmentId()));

        }

        @Test
        void deleteNotExistingPackageTest(){
            webTestClient.delete()
                    .uri(uriBuilder -> uriBuilder.path("api/packages/{id}").build(id+id1+id2))
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(ProblemDetail.class)
                    .value(problemDetail -> assertEquals(URI.create("packages/not-found"),problemDetail.getType()))
                    .value(problemDetail -> assertTrue(problemDetail.getDetail().contains(String.format("There is no package with the id: %s.",id+id1+id2))));


        }







    }


}
