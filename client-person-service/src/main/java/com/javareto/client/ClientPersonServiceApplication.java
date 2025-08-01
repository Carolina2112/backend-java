package com.javareto.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.javareto.client.repository")
public class ClientPersonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientPersonServiceApplication.class, args);
	}

}
