package com.currency.foreignexchange;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * This class is the entry point for the application, as well as a spring bean registry.
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Foreign Exchange Application API", version = "0.0.1",
        description = "The foreign exchange currency API provides a simple API for live currency rates and live currency conversions, " +
                "with the ability to save and read all the transactions made",
        contact = @Contact(name = "Valentin Dimitrov", email = "vdimitrov.work@gmail.com")))
public class ForeignExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForeignExchangeApplication.class, args);
    }

    @Bean
    public RestTemplate externalExchangeService(RestTemplateBuilder builder) {
        return builder.build();
    }

}
