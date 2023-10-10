package com.example.pladialmserver;

import com.example.pladialmserver.global.feign.feignClient.ArchivingServerClient;
import com.example.pladialmserver.global.response.ResponseCustom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;

@EnableScheduling
@EnableAsync
@EnableFeignClients(clients = {ArchivingServerClient.class})
@SpringBootApplication
public class PladiAlmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PladiAlmServerApplication.class, args);
    }
}
