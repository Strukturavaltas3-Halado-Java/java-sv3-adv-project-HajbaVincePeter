package com.example.restpost;

import com.example.restpost.mapper.AddressMapper;
//import com.example.restpost.mapper.AddressMapperImpl;
import com.example.restpost.mapper.AddressMapperImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestpostApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestpostApplication.class, args);
    }
    @Bean
    public AddressMapper addressMapper(){
        return new AddressMapperImpl();
    }




}
