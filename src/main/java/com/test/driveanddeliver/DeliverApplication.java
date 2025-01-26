package com.test.driveanddeliver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableR2dbcRepositories("com.test.driveanddeliver.infrastructure.repository")
@ConfigurationPropertiesScan
@EnableWebFluxSecurity
@OpenAPIDefinition
@EnableCaching
public class DeliverApplication {

    public static void main(String[] args) {

        SpringApplication.run(DeliverApplication.class, args);
    }
}
