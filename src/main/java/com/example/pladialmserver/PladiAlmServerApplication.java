package com.example.pladialmserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PladiAlmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PladiAlmServerApplication.class, args);
    }

}
