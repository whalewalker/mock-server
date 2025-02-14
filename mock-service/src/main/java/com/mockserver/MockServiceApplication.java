package com.mockserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MockServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockServiceApplication.class, args);
    }
}