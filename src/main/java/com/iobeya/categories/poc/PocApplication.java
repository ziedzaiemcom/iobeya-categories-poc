package com.iobeya.categories.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "iObeya Categories Server")})
@SpringBootApplication(exclude = {SessionAutoConfiguration.class})
@ComponentScan(basePackages = {"com.iobeya.categories.poc"})
public class PocApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocApplication.class, args);
	}
	
	@Bean
	public OpenAPI customOpenAPI() {
	   return new OpenAPI()
	    .info(new Info().title("iObeya Categories API").version("0.0.1").description("REST API for iObeya Categories"));
	}

}
