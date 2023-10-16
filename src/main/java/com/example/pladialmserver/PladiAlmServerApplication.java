package com.example.pladialmserver;

import com.example.pladialmserver.global.feign.feignClient.ArchivingServerClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableScheduling
@EnableAsync
@EnableJpaAuditing
@EnableFeignClients(clients = {ArchivingServerClient.class})
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PladiAlmServerApplication {

    @PostConstruct
    public void started() {
        // timezone UTC 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("KST"));
    }

    public static void main(String[] args) {
        SpringApplication.run(PladiAlmServerApplication.class, args);
    }

}
