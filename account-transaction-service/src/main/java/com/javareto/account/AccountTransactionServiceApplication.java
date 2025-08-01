package com.javareto.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AccountTransactionServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AccountTransactionServiceApplication.class, args);
    }
}