package com.gabrielcsilva1.Portal_Egresso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Portal Egresso",
		version = "1.0.0"
	)
)
public class PortalEgressoApplication {
	public static void main(String[] args) {
		SpringApplication.run(PortalEgressoApplication.class, args);
	}

}
