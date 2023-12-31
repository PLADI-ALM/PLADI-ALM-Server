package com.example.pladialmserver.global.feign.feignClient;

import com.example.pladialmserver.global.feign.dto.UserReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name="archiving-server", url = "${server.archiving.url}")
public interface ArchivingServerClient {
    @PostMapping("/users")
    void addUser(@RequestBody UserReq userReq);

    @PostMapping("/users/change")
    void changeUser(@RequestBody UserReq userReq);

    @DeleteMapping("/users")
    void deleteUser(@RequestBody UserReq userReq);
}
