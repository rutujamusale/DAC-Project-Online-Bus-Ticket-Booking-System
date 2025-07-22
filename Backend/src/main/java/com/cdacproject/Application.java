package com.cdacproject;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Bus Booking System API",
        version = "1.0",
        description = "Online Bus Ticket Booking System REST API",
        contact = @Contact(
            name = "Bus Booking Team",
            email = "support@busbooking.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Development Server")
    }
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ModelMapper modelMapper() {
        System.out.println("creating model mapper");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setPropertyCondition(Conditions.isNotNull());
        return mapper;
    }
}
