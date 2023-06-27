package com.example.restpost.model.address;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Country {
  HU("HUNGARY", 4),PL("POLAND",5),IE("IRELAND"),
    FI("FINLAND",5), FR("FRANCE",5), AT("AUSTRIA",4) ,
    BE("BELGIUM",4), BG("BULGARIA",4);

  final String name;

  Integer postalCodeFormat;

  Country(String name, int postalCodeFormat){
      this.name = name;this.postalCodeFormat = postalCodeFormat;
  }

    Country(String name) {
        this.name = name;
    }

    @Data
    public static class CountryData {
        private List<Country> countries = List.of(Country.values());
        private List<String> countryNames=  Arrays.stream(Country.values()).map(Country::getName).toList();
        private List<Integer> postalCodeFormats = Arrays.stream(Country.values()).map(Country::getPostalCodeFormat).toList();
    }



}
