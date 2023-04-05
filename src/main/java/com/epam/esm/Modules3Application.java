package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@ImportAutoConfiguration(FeignAutoConfiguration.class)
@SpringBootApplication
@EnableJpaAuditing
public class Modules3Application {

    public static void main(String[] args) {
        SpringApplication.run(Modules3Application.class, args);
    }
}