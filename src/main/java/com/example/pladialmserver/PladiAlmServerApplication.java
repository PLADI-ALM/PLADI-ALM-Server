package com.example.pladialmserver;

import com.example.pladialmserver.global.feign.feignClient.ArchivingServerClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@EnableFeignClients(clients = {ArchivingServerClient.class})
@SpringBootApplication
public class PladiAlmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PladiAlmServerApplication.class, args);
    }
}
