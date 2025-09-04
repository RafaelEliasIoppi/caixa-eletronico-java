package com.caixa.caixa_eletronico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	servers = {
		@Server(url = "https://urban-acorn-g4qrqppjw7p43jq9-3335.app.github.dev/")
	}
)
@SpringBootApplication
public class CaixaEletronicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaixaEletronicoApplication.class, args);
	}

}
