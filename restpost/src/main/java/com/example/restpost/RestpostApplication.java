package com.example.restpost;
import com.example.restpost.dtos.Analyzer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class RestpostApplication {



    public static void main(String[] args) {

              SpringApplication.run(RestpostApplication.class, args);
    }

    @Bean
    public Analyzer helper() { return  new Analyzer();}

}
