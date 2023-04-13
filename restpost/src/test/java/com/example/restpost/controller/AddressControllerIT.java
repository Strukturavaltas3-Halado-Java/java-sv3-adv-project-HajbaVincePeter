package com.example.restpost.controller;

import com.example.restpost.dtos.address_commands.CountryCommand;
import com.example.restpost.dtos.address_commands.UpdateIrishCommand;
import com.example.restpost.dtos.address_commands.UpdatePostalCommand;
import com.example.restpost.dtos.address_dtos.AddressIrishDto;
import com.example.restpost.dtos.address_dtos.AddressWithPostalCodeDto;
import com.example.restpost.model.address.AddressWithPostalCode;
import com.example.restpost.model.address.Country;
import com.example.restpost.model.address.County;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answer1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Sql(scripts = {"/cleartables.sql"})
public class AddressControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void createNewPostalAddressTest() {

        String body = "{ \"country\":\"HU\" }";

        webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddressWithPostalCodeDto.class)
                .value(addressWithPostalCode -> assertEquals(Country.HU, addressWithPostalCode.getCountry()))
                .value(addressWithPostalCode -> assertNotNull(addressWithPostalCode.getId()))
                .value(addressWithPostalCode -> assertNull(addressWithPostalCode.getPostalCode()))
                .value(addressWithPostalCode -> assertNull(addressWithPostalCode.getStreetAddress()))
                .value(addressWithPostalCode -> assertNull(addressWithPostalCode.getCity()))
                .value(addressWithPostalCode -> assertNull(addressWithPostalCode.getName()));

    }


    @Test
    void createNewIrishAddressTest() {
        String body = "{ \"country\":\"IE\" }";
        webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddressIrishDto.class)
                .value(addressIrishDto -> assertEquals(Country.IE, addressIrishDto.getCountry()))
                .value(addressWithPostalCode -> assertNotNull(addressWithPostalCode.getId()))
                .value(addressIrishDto -> assertNull(addressIrishDto.getCounty()))
                .value(addressIrishDto -> assertNull(addressIrishDto.getStreetAddress()))
                .value(addressIrishDto -> assertNull(addressIrishDto.getCity()))
                .value(addressIrishDto -> assertNull(addressIrishDto.getName()));

    }

    @Test
    void wrongCountryCodeTest() {

        String body = "{ \"country\":\"DE\" }";


        webTestClient.post()
                .uri("api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail ->
                        assertEquals(URI.create("addresses/value-not-valid"), problemDetail.getType()))
                .value(problemDetail ->
                        assertEquals("JSON parse error: Cannot deserialize value of type " +
                                "`com.example.restpost.model.address.Country` from String \"DE\": " +
                                "not one of the values accepted for Enum class: [PL, HU, IE]", problemDetail.getDetail()));

        System.out.println(body.getClass().getSimpleName());
    }


    @Test
    void updatePostalAddressTest() {

        String body = "{ \"country\":\"HU\" }";

        Long id = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectBody(AddressWithPostalCodeDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/non-ie").build(id))
                .bodyValue(new UpdatePostalCommand(Country.HU, "Valamilyen utca 6.", "Teleki Blanka", "Budapest", "1126"))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(AddressWithPostalCodeDto.class)
                .value(addressWithPostalCodeDto ->
                        Assertions.assertThat(addressWithPostalCodeDto).extracting(AddressWithPostalCodeDto::getCountry).isEqualTo(Country.HU))
                .value(addressWithPostalCodeDto ->
                        Assertions.assertThat(addressWithPostalCodeDto).extracting(AddressWithPostalCodeDto::getPostalCode).isEqualTo("1126"))
                .value(addressWithPostalCodeDto ->
                        Assertions.assertThat(addressWithPostalCodeDto).extracting(AddressWithPostalCodeDto::getStreetAddress).isEqualTo("Valamilyen utca 6."))
                .value(addressWithPostalCodeDto ->
                        Assertions.assertThat(addressWithPostalCodeDto).extracting(AddressWithPostalCodeDto::getCity).isEqualTo("Budapest"))
                .value(addressWithPostalCodeDto ->
                        Assertions.assertThat(addressWithPostalCodeDto).extracting(AddressWithPostalCodeDto::getName).isEqualTo("Teleki Blanka"));

    }


    @Test
    void updateIrishAddressTest() {

        String body = "{ \"country\":\"IE\" }";

        Long id = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(id))
                .bodyValue(new UpdateIrishCommand( "Main street 6.", "G.B Show", "Dublin", County.Dublin))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .value(addressIrishDto ->
                        Assertions.assertThat(addressIrishDto).extracting(AddressIrishDto::getCountry).isEqualTo(Country.IE))
                .value(addressIrishDto ->
                        Assertions.assertThat(addressIrishDto).extracting(AddressIrishDto::getCounty).isEqualTo(County.Dublin))
                .value(addressIrishDto ->
                        Assertions.assertThat(addressIrishDto).extracting(AddressIrishDto::getStreetAddress).isEqualTo("Main street 6."))
                .value(addressIrishDto ->
                        Assertions.assertThat(addressIrishDto).extracting(AddressIrishDto::getCity).isEqualTo("Dublin"))
                .value(addressIrishDto ->
                        Assertions.assertThat(addressIrishDto).extracting(AddressIrishDto::getName).isEqualTo("G.B Show"));

    }


}
