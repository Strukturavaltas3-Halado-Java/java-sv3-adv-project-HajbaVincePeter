package com.example.restpost.controller;


import com.example.restpost.dtos.address_commands.UpdateIrishCommand;
import com.example.restpost.dtos.address_commands.UpdatePostalCommand;

import com.example.restpost.dtos.address_dtos.AddressIrishDto;
import com.example.restpost.dtos.address_dtos.AddressWithPostalCodeDto;

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
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

        String body = "{ \"country\":\"XX\" }";


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
                                "`com.example.restpost.model.address.Country` from String \"XX\": " +
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
                .bodyValue(new UpdateIrishCommand("Main street 6.", "G.B Show", "Dublin", County.Dublin))
                .exchange()
                .expectStatus().isAccepted()
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


    @Test
    void wrongPostalAddressUpdate() {

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
                .bodyValue(new UpdatePostalCommand(
                        Country.HU, "Valamilyen utca 6.", "Teleki Blanka",
                        "Budapest", "01126"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains("wrong postal code format for the given country")))
                .value(problemDetail -> assertTrue(problemDetail.getType().equals(URI.create("wrong-data"))));


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/non-ie").build(id))
                .bodyValue(new UpdatePostalCommand(
                        Country.IE, "Valamilyen utca 6.", "Teleki Blanka",
                        "Budapest", "1126"))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains("wrong postal code format for the given country")))
                .value(problemDetail -> assertTrue(problemDetail.getType().equals(URI.create("wrong-data"))));

    }

    @Test
    void countryMisMatch() {

        String body = "{ \"country\":\"HU\" }";

        Long id = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectBody(AddressWithPostalCodeDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(id))
                .bodyValue(new UpdateIrishCommand(
                        "Valamilyen utca 6.", "Teleki Blanka",
                        "Budapest", County.Cork))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains(String.format("The type of the country of the address with the id: %s cannot be updated with this update.",id))))
                .value(problemDetail -> assertTrue(problemDetail.getType().equals(URI.create("addresses/not-for-this-country"))));

    }

    @Test
    void wrongIrishAddressUpdate() {

        String body1 = "{ \"country\":\"IE\" }";

        Long id = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body1))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();


        String body2 = "{ \"streetAddress\":\"Main street 6.\", \"name\":\"G.B Show\",\"city\":\"Dublin\",\"county\":\"Belfast\" }";

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(id))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body2))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail ->
                        assertEquals(URI.create("addresses/value-not-valid"), problemDetail.getType()))
                .value(problemDetail ->
                        assertEquals("JSON parse error: Cannot deserialize value of type " +
                                "`com.example.restpost.model.address.County` from String \"Belfast\": " +
                                "not one of the values accepted for Enum class: [Carlow, Roscommon, Monaghan, " +
                                "Laois, Cork, Louth, Mayo, Clare, Westmeath, Donegal, Kilkenny, Waterford, " +
                                "Limerick, Longford, Sligo, Wicklow, Galway, Kildare, Offaly, Dublin, Wexford, " +
                                "Tipperary, Leitrim, Meath, Kerry, Cavan]", problemDetail.getDetail()));


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(id))
                .bodyValue(new UpdateIrishCommand(null, "G.B Show", "Dublin", County.Dublin))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail ->
                        assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains(" [Field error in object 'updateIrishCommand' on field 'streetAddress': rejected value " +
                                "[null]; codes [NotNull.updateIrishCommand.streetAddress,NotNull.streetAddress,NotNull.java.lang.String,NotNull]; " +
                                "arguments [org.springframework.context.support.DefaultMessageSourceResolvable: " +
                                "codes [updateIrishCommand.streetAddress,streetAddress]; arguments []; default message [streetAddress]]")));


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(id))
                .bodyValue(new UpdateIrishCommand("Main street 6.", "G.B Show", " ", County.Dublin))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406, problemDetail.getStatus()))
                .value(problemDetail ->
                        assertEquals(URI.create("wrong-data"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains("[Field error in object 'updateIrishCommand' on field 'city': rejected value [ ]; " +
                                "codes [NotBlank.updateIrishCommand.city,NotBlank.city,NotBlank.java.lang.String,NotBlank]; " +
                                "arguments [org.springframework.context.support.DefaultMessageSourceResolvable: " +
                                "codes [updateIrishCommand.city,city]; arguments []; default message [city]]")));

    }

    @Nested
    @Sql(scripts = {"/cleartables.sql"})
    class getTests {

        Long id;
        Long id1;

        @BeforeEach
        void init() {


            String body = "{ \"country\":\"HU\" }";

            id = webTestClient.post()
                    .uri("/api/addresses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(body))
                    .exchange()
                    .expectBody(AddressWithPostalCodeDto.class)
                    .returnResult().getResponseBody().getId();


            webTestClient.put()
                    .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/non-ie").build(id))
                    .bodyValue(new UpdatePostalCommand(Country.HU, "Valamilyen utca 6.", "Teleki Blanka", "Budapest", "1126"))
                    .exchange();

            String body1 = "{ \"country\":\"IE\" }";

            id1 = webTestClient.post()
                    .uri("/api/addresses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(body1))
                    .exchange()
                    .expectBody(AddressIrishDto.class)
                    .returnResult().getResponseBody().getId();


            webTestClient.put()
                    .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(id1))
                    .bodyValue(new UpdateIrishCommand("Main street 6.", "G.B Show", "Dublin", County.Dublin))
                    .exchange();


        }

        @Test
        void getAddressListTest() {

            List<Object> result = webTestClient.get()
                    .uri("/api/addresses")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectBodyList(Object.class)
                    .returnResult().getResponseBody();

            Assertions.assertThat(result).extracting(Object::toString).containsOnly(
                    String.format("{id=%s, country=HU, streetAddress=Valamilyen utca 6., name=Teleki Blanka, city=Budapest, postalCode=1126}", id),
                    String.format("{id=%s, country=IE, streetAddress=Main street 6., name=G.B Show, city=Dublin, county=Dublin}", id1));

        }


        @Test
        void getPostalAddressById() {

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}").build(id))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(AddressWithPostalCodeDto.class)
                    .value(addressWithPostalCodeDto -> assertEquals("1126", addressWithPostalCodeDto.getPostalCode()))
                    .value(addressWithPostalCodeDto -> assertEquals("Teleki Blanka", addressWithPostalCodeDto.getName()))
                    .value(addressWithPostalCodeDto -> assertEquals("Valamilyen utca 6.", addressWithPostalCodeDto.getStreetAddress()))
                    .value(addressWithPostalCodeDto -> assertEquals("Budapest", addressWithPostalCodeDto.getCity()))
                    .value(addressWithPostalCodeDto -> assertEquals(Country.HU, addressWithPostalCodeDto.getCountry()));

        }

        @Test
        void getPostalIrishAddressTest() {

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}").build(id1))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(AddressIrishDto.class)
                    .value(addressIrishDto -> assertEquals(County.Dublin, addressIrishDto.getCounty()))
                    .value(addressIrishDto -> assertEquals("G.B Show", addressIrishDto.getName()))
                    .value(addressIrishDto -> assertEquals("Main street 6.", addressIrishDto.getStreetAddress()))
                    .value(addressIrishDto -> assertEquals("Dublin", addressIrishDto.getCity()))
                    .value(addressIrishDto -> assertEquals(Country.IE, addressIrishDto.getCountry()));
        }


    }


    @Test
    void deleteAddressTest() {


        String body1 = "{ \"country\":\"IE\" }";

        Long id1 = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body1))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();


        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}/ie").build(id1))
                .bodyValue(new UpdateIrishCommand("Main street 6.", "G.B Show", "Dublin", County.Dublin))
                .exchange();


        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}").build(id1))
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(AddressIrishDto.class)
                .value(addressIrishDto -> assertEquals(County.Dublin, addressIrishDto.getCounty()))
                .value(addressIrishDto -> assertEquals("G.B Show", addressIrishDto.getName()))
                .value(addressIrishDto -> assertEquals("Main street 6.", addressIrishDto.getStreetAddress()))
                .value(addressIrishDto -> assertEquals("Dublin", addressIrishDto.getCity()))
                .value(addressIrishDto -> assertEquals(Country.IE, addressIrishDto.getCountry()));
    }


    @Test
    void deleteAddressInShipmentTest(){


        String body1 = "{ \"country\":\"IE\" }";

        Long addressId = webTestClient.post()
                .uri("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(body1))
                .exchange()
                .expectBody(AddressIrishDto.class)
                .returnResult().getResponseBody().getId();

        Long shipmentId = webTestClient.post()
                .uri("/api/shipments")
                .exchange()
                .expectBody(ShipmentDto.class)
                .returnResult().getResponseBody().getId();

        UpdateShipmentCommand shipmentCommand = new UpdateShipmentCommand();

        shipmentCommand.setId(shipmentId);
        shipmentCommand.setFromId(addressId);

        webTestClient.put()
                .uri("api/shipments")
                .bodyValue(shipmentCommand)
                .exchange();



        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}").build(addressId))
                .exchange()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> assertEquals(406,problemDetail.getStatus()))
                .value(problemDetail ->
                        assertEquals(URI.create("addresses/in-use"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains(String.format("There is(are) %s shipment(s) containing the address with id: %s",1,addressId))));

    }



    @Test
    @Sql(scripts = {"/cleartables.sql"})
    void deleteNotExistingAddressTest() {

        Long id1 = 1L;

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/{id}").build(id1))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProblemDetail.class)
                .value(problemDetail ->
                        assertEquals(URI.create("addresses/address-not-found"), problemDetail.getType()))
                .value(problemDetail -> assertTrue(problemDetail.getDetail()
                        .contains(String.format("The address with the id %s does not exist", id1))));
    }

    @Test
    void getCountriesTest() {


        Object result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/countries").build())
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Object.class)
                .returnResult().getResponseBody();

        assertEquals("{countries=[HU, PL, IE], countryNames=[HUNGARY, POLAND, IRELAND], postalCodeFormats=[4, 5, null]}",
                result.toString());


    }

    @Test
    void getCountiesTest() {


        Object result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/addresses/counties").build())
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Object.class)
                .returnResult().getResponseBody();

        assertEquals("[Carlow, Cavan, Clare, Cork, Donegal, Dublin, Galway, Kerry, Kildare, Kilkenny, Laois, " +
                        "Leitrim, Limerick, Longford, Louth, Mayo, Meath, Monaghan, Offaly, Roscommon, Sligo, Tipperary, " +
                        "Waterford, Westmeath, Wexford, Wicklow]",
                result.toString());

    }


}
