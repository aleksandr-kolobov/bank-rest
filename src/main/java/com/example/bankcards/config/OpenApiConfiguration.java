package com.example.bankcards.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPIDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl("http://localhost:8080/");
        localhostServer.setDescription("Local env");

        Server productionServer = new Server();
        productionServer.setUrl("http://bankcards.url");
        productionServer.setDescription("Production env");

        Contact contact = new Contact();
        contact.setName("Alex Kolo");
        contact.setEmail("alexkolo1234@bankcards.com");
        contact.setUrl("http://bankcards.test.url");

        License mitLicense = new License()
                .name("GNU AGPLv3")
                .url("https://choosealicense.com/license/agpl-3.0");

        Info info = new Info()
                .title("Bankcards Service API")
                .version("1.0.0")
                .contact(contact)
                .description("API for Bankcards Service")
                .termsOfService("http://bankcards.terms.url")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(localhostServer, productionServer));
    }
}
